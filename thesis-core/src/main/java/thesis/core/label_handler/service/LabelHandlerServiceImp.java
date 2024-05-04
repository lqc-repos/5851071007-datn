package thesis.core.label_handler.service;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.article.Article;
import thesis.core.article.command.CommandCommonQuery;
import thesis.core.article.model.author.Author;
import thesis.core.article.model.author.service.AuthorService;
import thesis.core.article.model.label.custom_label.CustomLabel;
import thesis.core.article.model.label.custom_label.service.CustomLabelService;
import thesis.core.article.model.location.Location;
import thesis.core.article.model.location.service.LocationService;
import thesis.core.article.model.topic.Topic;
import thesis.core.article.model.topic.service.TopicService;
import thesis.core.article.service.ArticleService;
import thesis.core.configuration.service.ThesisConfigurationService;
import thesis.core.crawler.crawled_article.CrawledArticle;
import thesis.core.crawler.crawled_article.command.CommandQueryCrawledArticle;
import thesis.core.crawler.crawled_article.service.CrawledArticleService;
import thesis.core.label_handler.dto.ArticleIdfInfo;
import thesis.core.label_handler.dto.ArticleWorldInfo;
import thesis.core.label_handler.model.article_label_frequency.ArticleLabelFrequency;
import thesis.core.label_handler.model.article_label_frequency.command.CommandQueryArticleLabelFrequency;
import thesis.core.label_handler.model.article_label_frequency.service.ArticleLabelFrequencyService;
import thesis.core.label_handler.model.total_label_frequency.TotalLabelFrequency;
import thesis.core.label_handler.model.total_label_frequency.command.CommandQueryTotalLabelFrequency;
import thesis.core.label_handler.model.total_label_frequency.service.TotalLabelFrequencyService;
import thesis.core.nlp.dto.AnnotatedWord;
import thesis.core.nlp.service.NLPService;
import thesis.utils.constant.ConfigurationName;
import thesis.utils.file.CSVExporter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class LabelHandlerServiceImp implements LabelHandlerService {
    private static final List<String> NER_TYPES = Arrays.asList("B-PER", "B-ORG", "B-LOC", "I-PER", "I-ORG", "I-LOC");

    @Autowired
    private CrawledArticleService crawledArticleService;
    @Autowired
    private ArticleLabelFrequencyService articleLabelFrequencyService;
    @Autowired
    private TotalLabelFrequencyService totalLabelFrequencyService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private NLPService nlpService;
    @Autowired
    private ThesisConfigurationService thesisConfigurationService;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private CustomLabelService customLabelService;

    /**
     * Use to migrate crawled article to article collection
     */
    @Override
    public Optional<Boolean> migrateArticle() {
        Set<String> urls = new HashSet<>();
        Long totalCrawledArticle = crawledArticleService.count(CommandQueryCrawledArticle.builder().build()).orElseThrow();
        int sizePerPage = 50, totalPage = (int) ((totalCrawledArticle + sizePerPage - 1) / sizePerPage);
        for (int i = 0; i < totalPage; i++) {
            List<CrawledArticle> crawledArticles = crawledArticleService.getMany(CommandQueryCrawledArticle.builder()
                    .isDescPublicationDate(false)
                    .page(i)
                    .size(sizePerPage)
                    .build());
            List<Article> articles = new ArrayList<>(crawledArticles.stream().map(crawledArticle -> Article.builder()
                    .url(crawledArticle.getUrl())
                    .title(crawledArticle.getTitle().replaceAll("_", "-"))
                    .location(crawledArticle.getLocation())
                    .description(crawledArticle.getDescription().replaceAll("_", "-"))
                    .content(crawledArticle.getContent().replaceAll("_", "-"))
                    .publicationDate(crawledArticle.getPublicationDate())
                    .authors(crawledArticle.getAuthors())
                    .topics(crawledArticle.getTopics())
                    .labels(crawledArticle.getLabels())
                    .build()).toList());
            articles.removeIf(art -> urls.contains(art.getUrl()));
            if (CollectionUtils.isNotEmpty(articles))
                articleService.addMany(articles);
            urls.addAll(articles.stream().map(Article::getUrl).toList());
        }
        return Optional.of(Boolean.TRUE);
    }

    /**
     * Get NLP labels and store labels into article_label_frequency and total_label_frequency collections
     */
    @Override
    public Optional<Boolean> storeFrequency() {
        Set<String> existedTotalLabels = totalLabelFrequencyService.getExistedLabel();
        Long totalArticle = articleService.count(CommandCommonQuery.builder().build()).orElseThrow();
        int sizePerPage = 100, totalPage = (int) ((totalArticle + sizePerPage - 1) / sizePerPage);
        log.info("=== total page: {}", totalPage);
        Set<String> articleUrls = new HashSet<>();
        for (int i = 0; i < totalPage; i++) {
            log.info("=== current page: {}", i + 1);
            List<ArticleLabelFrequency> articleLabelFrequencies = new ArrayList<>();
            Map<String, Long> totalArticleLabels = new HashMap<>();
            List<Article> articles = articleService.getMany(CommandCommonQuery.builder()
                    .isDescId(true)
                    .page(i + 1)
                    .size(sizePerPage)
                    .build());
            for (Article article : articles) {
                try {
                    if (articleUrls.contains(article.getUrl()))
                        continue;
                    String contentBuilder = article.getTitle().trim() + "\n" +
                            article.getDescription().trim() + "\n" +
                            article.getContent().trim();
                    Optional<AnnotatedWord> annotatedWordOptional = nlpService.annotate(contentBuilder);
                    if (annotatedWordOptional.isEmpty()) {
                        log.warn("Cannot annotate articleId {}", article.getId().toString());
                        continue;
                    }
                    AnnotatedWord annotatedWord = annotatedWordOptional.get();
                    Map<String, ArticleWorldInfo> articleWorldInfoMap = new HashMap<>();
                    annotatedWord.getTaggedWords().forEach(taggedWord -> {
                        ArticleWorldInfo worldInfo = articleWorldInfoMap.getOrDefault(taggedWord.getWord(),
                                ArticleWorldInfo.builder()
                                        .ner(taggedWord.getNer())
                                        .count(0L)
                                        .build());
                        worldInfo.incrementCount();
                        articleWorldInfoMap.put(taggedWord.getWord(), worldInfo);
                    });
                    articleWorldInfoMap.keySet().forEach(key -> {
                        if (!totalArticleLabels.containsKey(key))
                            totalArticleLabels.put(key, 1L);
                        else
                            totalArticleLabels.put(key, totalArticleLabels.get(key) + 1);
                    });
                    long totalLabel = articleWorldInfoMap.values().stream().mapToLong(ArticleWorldInfo::getCount).sum();
                    List<ArticleLabelFrequency.LabelPerArticle> labelPerArticles = articleWorldInfoMap.entrySet().stream()
                            .map(k -> ArticleLabelFrequency.LabelPerArticle.builder()
                                    .label(k.getKey())
                                    .count(k.getValue().getCount())
                                    .ner(k.getValue().getNer())
                                    .build())
                            .sorted(Comparator.comparing(ArticleLabelFrequency.LabelPerArticle::getCount).reversed())
                            .toList();
                    ArticleLabelFrequency articleLabelFrequency = ArticleLabelFrequency.builder()
                            .articleId(article.getId().toString())
                            .totalLabel(totalLabel)
                            .labels(labelPerArticles)
                            .build();
                    articleLabelFrequencies.add(articleLabelFrequency);
                    articleUrls.add(article.getUrl());
                } catch (Exception ex) {
                    log.warn("Annotate is error, article url: {}", article.getUrl());
                }
            }
            articleLabelFrequencyService.addMany(articleLabelFrequencies);
            totalLabelFrequencyService.increase(existedTotalLabels, totalArticleLabels);
            log.info("=== end page: {} - articleLabels: {} - totalByLabel: {}", i + 1, articleLabelFrequencies.size(), totalArticleLabels.size());
        }
        return Optional.of(Boolean.TRUE);
    }

    /**
     * Divide article components, ex: author, topic, labels,...
     */
    @Override
    public Optional<Boolean> divideArticleComponent() {
        Long totalArticle = articleService.count(CommandCommonQuery.builder().build()).orElseThrow();
        int sizePerPage = 100, totalPage = (int) ((totalArticle + sizePerPage - 1) / sizePerPage);
        log.info("=== total page: {}", totalPage);
        Set<String> articleUrls = new HashSet<>();
        // author
        Map<String, Author> authorMap = new HashMap<>();
        {
            List<Author> authors = authorService.getMany(CommandCommonQuery.builder()
                    .page(1)
                    .size(Integer.MAX_VALUE)
                    .build());
            authors.forEach(author -> authorMap.put(author.getAuthor(), author));
        }
        // topic
        Map<String, Topic> topicMap = new HashMap<>();
        {
            List<Topic> topics = topicService.getMany(CommandCommonQuery.builder()
                    .page(1)
                    .size(Integer.MAX_VALUE)
                    .build());
            topics.forEach(topic -> topicMap.put(topic.getTopic(), topic));
        }
        // location
        Map<String, Location> locationMap = new HashMap<>();
        {
            List<Location> locations = locationService.getMany(CommandCommonQuery.builder()
                    .page(1)
                    .size(Integer.MAX_VALUE)
                    .build());
            locations.forEach(location -> locationMap.put(location.getLocation(), location));
        }
        // custom labels
        Map<String, CustomLabel> customLabelMap = new HashMap<>();
        {
            List<CustomLabel> customLabels = customLabelService.getMany(CommandCommonQuery.builder()
                    .page(1)
                    .size(Integer.MAX_VALUE)
                    .build());
            customLabels.forEach(customLabel -> customLabelMap.put(customLabel.getLabel(), customLabel));
        }

        for (int i = 0; i < totalPage; i++) {
            log.info("=== current page: {}", i + 1);
            List<Article> articles = articleService.getMany(CommandCommonQuery.builder()
                    .isDescId(true)
                    .page(i + 1)
                    .size(sizePerPage)
                    .build());
            for (Article article : articles) {
                try {
                    if (articleUrls.contains(article.getUrl()))
                        continue;

                    // handle author
                    List<String> authorList = article.getAuthors();
                    if (CollectionUtils.isNotEmpty(authorList)) {
                        for (String authorStr : authorList) {
                            if (StringUtils.isBlank(authorStr))
                                continue;
                            String authorKey = authorStr.toLowerCase().replaceAll(" ", "_");
                            Author author = authorMap.getOrDefault(authorKey, Author.builder()
                                    .author(authorKey)
                                    .articleIds(new HashSet<>())
                                    .build());
                            author.getArticleIds().add(article.getId().toHexString());
                            authorMap.put(author.getAuthor(), author);
                        }
                    }
                    //handle topic
                    List<String> topicList = article.getTopics();
                    if (CollectionUtils.isNotEmpty(topicList)) {
                        for (String topicStr : topicList) {
                            if (StringUtils.isBlank(topicStr))
                                continue;
                            String topicKey = topicStr.toLowerCase().replaceAll(" ", "_");
                            Topic topic = topicMap.getOrDefault(topicKey, Topic.builder()
                                    .topic(topicKey)
                                    .articleIds(new HashSet<>())
                                    .build());
                            topic.getArticleIds().add(article.getId().toHexString());
                            topicMap.put(topic.getTopic(), topic);
                        }
                    }
                    //handle location
                    String locationStr = article.getLocation();
                    if (StringUtils.isNotBlank(locationStr)) {
                        String locationKey = locationStr.toLowerCase().replaceAll(" ", "_");
                        Location location = locationMap.getOrDefault(locationKey, Location.builder()
                                .location(locationKey)
                                .articleIds(new HashSet<>())
                                .build());
                        location.getArticleIds().add(article.getId().toHexString());
                        locationMap.put(location.getLocation(), location);
                    }
                    //handle custom labels
                    List<String> customLabelList = article.getLabels();
                    if (CollectionUtils.isNotEmpty(customLabelList)) {
                        for (String customLabelStr : customLabelList) {
                            if (StringUtils.isBlank(customLabelStr))
                                continue;
                            String customLabelKey = customLabelStr.toLowerCase().replaceAll(" ", "_");
                            CustomLabel customLabel = customLabelMap.getOrDefault(customLabelKey, CustomLabel.builder()
                                    .label(customLabelKey)
                                    .articleIds(new HashSet<>())
                                    .build());
                            customLabel.getArticleIds().add(article.getId().toHexString());
                            customLabelMap.put(customLabel.getLabel(), customLabel);
                        }
                    }
                    // add article url
                    articleUrls.add(article.getUrl());
                } catch (Exception ex) {
                    log.warn("Can not divide article: {}", article.getId().toString());
                }
            }
            log.info("=== end page: {}", i + 1);
        }
        // insert all
        authorService.addMany(authorMap.values().stream().toList());
        topicService.addMany(topicMap.values().stream().toList());
        locationService.addMany(locationMap.values().stream().toList());
        customLabelService.addMany(customLabelMap.values().stream().toList());
        return Optional.of(Boolean.TRUE);
    }

    /**
     * Calculate TF rate = number of label / sum of labels
     */
    @Override
    public Optional<Boolean> calculateTf() throws Exception {
        long totalArticleLabels = articleLabelFrequencyService.count().orElseThrow();
        int sizePerPage = 100, totalPage = (int) ((totalArticleLabels + sizePerPage - 1) / sizePerPage);
        log.info("=== total page: {}", totalPage);
        for (int i = 0; i < totalPage; i++) {
            List<ArticleLabelFrequency> articleLabelFrequencies = articleLabelFrequencyService
                    .getMany(CommandQueryArticleLabelFrequency.builder()
                            .isDescId(true)
                            .page(i + 1)
                            .size(sizePerPage)
                            .build());
            log.info("=== current page: {}, size: {}", i + 1, articleLabelFrequencies.size());
            for (ArticleLabelFrequency articleLabelFrequency : articleLabelFrequencies) {
                try {
                    if (articleLabelFrequency.getTotalLabel() <= 0 || CollectionUtils.isEmpty(articleLabelFrequency.getLabels())) {
                        log.warn("Total label equals zero or label list is empty - articleId: {}", articleLabelFrequency.getArticleId());
                        continue;
                    }
                    for (ArticleLabelFrequency.LabelPerArticle labelPerArticle : articleLabelFrequency.getLabels()) {
                        double tf = BigDecimal.valueOf(labelPerArticle.getCount())
                                .divide(BigDecimal.valueOf(articleLabelFrequency.getTotalLabel()), 20, RoundingMode.CEILING).doubleValue();
                        labelPerArticle.setTf(tf);
                    }
                    articleLabelFrequencyService.updateOne(articleLabelFrequency).orElseThrow();
                } catch (Exception ex) {
                    log.warn("Calculate TF-IDF error with ArticleLabel id: {}", articleLabelFrequency.getId().toString());
                }
            }
            log.info("=== end page: {}", i + 1);
        }
        return Optional.of(Boolean.TRUE);
    }

    /**
     * Detect the article's nlp labels (PER, ORG, Normal label) and save them
     */
    @Override
    public Optional<Boolean> handleNLPLabel() throws Exception {
        List<TotalLabelFrequency> totalLabelFrequencies = totalLabelFrequencyService.getMany(CommandQueryTotalLabelFrequency.builder()
                .hasProjection(true)
                .totalLabelProjection(CommandQueryTotalLabelFrequency.TotalLabelProjection.builder()
                        .isId(false)
                        .isLabel(true)
                        .isCount(true)
                        .build())
                .build());
        if (CollectionUtils.isEmpty(totalLabelFrequencies))
            throw new Exception("Existed labels is empty");
        Map<String, Long> labelWithCountMap = totalLabelFrequencies.stream()
                .collect(Collectors.toMap(TotalLabelFrequency::getLabel, TotalLabelFrequency::getCount));

        double eligibleRate = thesisConfigurationService.getByName(ConfigurationName.ELIGIBLE_RATE.getName())
                .map(m -> Double.valueOf(m.getValue()))
                .orElse(0.05D);

        List<ArticleIdfInfo> articleIdfInfos = new ArrayList<>();

        long totalArticle = articleLabelFrequencyService.count().orElseThrow();
        int sizePerPage = 100, totalPage = (int) ((totalArticle + sizePerPage - 1) / sizePerPage);
        log.info("=== total page: {}", totalPage);
        for (int i = 0; i < totalPage; i++) {
            List<ArticleLabelFrequency> articleLabelFrequencies = articleLabelFrequencyService
                    .getMany(CommandQueryArticleLabelFrequency.builder()
                            .isDescId(true)
                            .page(i + 1)
                            .size(sizePerPage)
                            .build());
            log.info("=== current page: {}, size: {}", i + 1, articleLabelFrequencies.size());
            for (ArticleLabelFrequency articleLabelFrequency : articleLabelFrequencies) {
                ArticleIdfInfo articleIdfInfo = ArticleIdfInfo.builder()
                        .articleId(articleLabelFrequency.getArticleId())
                        .idfInfos(new ArrayList<>())
                        .build();
                for (ArticleLabelFrequency.LabelPerArticle labelPerArticle : articleLabelFrequency.getLabels()) {
                    double idf = Math.log(BigDecimal.valueOf(totalArticle)
                            .divide(BigDecimal.valueOf(labelWithCountMap.get(labelPerArticle.getLabel())), 20, RoundingMode.CEILING)
                            .doubleValue());
                    double tfIdf = BigDecimal.valueOf(labelPerArticle.getTf())
                            .multiply(BigDecimal.valueOf(idf))
                            .setScale(20, RoundingMode.CEILING).doubleValue();
                    if (tfIdf >= eligibleRate || NER_TYPES.contains(labelPerArticle.getNer())) {
                        articleIdfInfo.getIdfInfos().add(ArticleIdfInfo.IdfInfo.builder()
                                .label(labelPerArticle.getLabel())
                                .ner(labelPerArticle.getNer())
                                .tf(labelPerArticle.getTf())
                                .idf(idf)
                                .tfIdf(tfIdf)
                                .build());
                    }
                }
                articleIdfInfos.add(articleIdfInfo);
            }
            log.info("=== end page: {}", i + 1);
        }
        CSVExporter.exportTfIdfRate(articleIdfInfos);
        return Optional.of(Boolean.TRUE);
    }
}
