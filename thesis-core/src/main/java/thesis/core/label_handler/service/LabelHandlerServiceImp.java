package thesis.core.label_handler.service;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.article.Article;
import thesis.core.article.command.CommandQueryArticle;
import thesis.core.article.service.ArticleService;
import thesis.core.crawler.crawled_article.CrawledArticle;
import thesis.core.crawler.crawled_article.command.CommandQueryCrawledArticle;
import thesis.core.crawler.crawled_article.service.CrawledArticleService;
import thesis.core.label_handler.dto.ArticleWorldInfo;
import thesis.core.label_handler.model.article_label_frequency.ArticleLabelFrequency;
import thesis.core.label_handler.model.article_label_frequency.command.CommandQueryArticleLabelFrequency;
import thesis.core.label_handler.model.article_label_frequency.service.ArticleLabelFrequencyService;
import thesis.core.label_handler.model.total_label_frequency.TotalLabelFrequency;
import thesis.core.label_handler.model.total_label_frequency.command.CommandQueryTotalLabelFrequency;
import thesis.core.label_handler.model.total_label_frequency.service.TotalLabelFrequencyService;
import thesis.core.nlp.dto.AnnotatedWord;
import thesis.core.nlp.service.NLPService;
import thesis.utils.file.CSVExporter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class LabelHandlerServiceImp implements LabelHandlerService {
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

    @Override
    public Optional<Boolean> storageFrequency() {
        Set<String> existedTotalLabels = totalLabelFrequencyService.getExistedLabel();
        Long totalArticle = articleService.count(CommandQueryArticle.builder().build()).orElseThrow();
        int sizePerPage = 100, totalPage = (int) ((totalArticle + sizePerPage - 1) / sizePerPage);
        log.info("=== total page: {}", totalPage);
        Set<String> articleUrls = new HashSet<>();
        for (int i = 0; i < totalPage; i++) {
            log.info("=== current page: {}", i + 1);
            List<ArticleLabelFrequency> articleLabelFrequencies = new ArrayList<>();
            Map<String, Long> totalArticleLabels = new HashMap<>();
            List<Article> articles = articleService.getMany(CommandQueryArticle.builder()
                    .isDescPublicationDate(true)
                    .isDescCreatedDate(true)
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
            log.info("=== end page: {} - articleAlgorithmLabels: {} - totalByLabel: {}", i + 1, articleLabelFrequencies.size(), totalArticleLabels.size());
        }
        return Optional.of(Boolean.TRUE);
    }

    @Override
    public Optional<Boolean> calculateTfIdf() throws Exception {
        List<TotalLabelFrequency> totalLabelFrequencies = totalLabelFrequencyService.get(CommandQueryTotalLabelFrequency.builder()
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
        Long totalArticleLabels = articleLabelFrequencyService.count().orElseThrow();
        int sizePerPage = 100, totalPage = (int) ((totalArticleLabels + sizePerPage - 1) / sizePerPage);
        log.info("=== total page: {}", totalPage);
        for (int i = 0; i < totalPage; i++) {
            log.info("=== current page: {}", i + 1);
            List<ArticleLabelFrequency> articleLabelFrequencies = articleLabelFrequencyService
                    .getMany(CommandQueryArticleLabelFrequency.builder()
                            .isDescCreatedDate(true)
                            .page(i + 1)
                            .size(sizePerPage)
                            .build());
            for (ArticleLabelFrequency articleLabelFrequency : articleLabelFrequencies) {
                try {
                    if (articleLabelFrequency.getTotalLabel() <= 0 || CollectionUtils.isEmpty(articleLabelFrequency.getLabels())) {
                        log.warn("Total label equals zero or label list is empty - articleId: {}", articleLabelFrequency.getArticleId());
                        continue;
                    }
                    for (ArticleLabelFrequency.LabelPerArticle labelPerArticle : articleLabelFrequency.getLabels()) {
                        double tf = BigDecimal.valueOf(labelPerArticle.getCount())
                                .divide(BigDecimal.valueOf(articleLabelFrequency.getTotalLabel()), 20, RoundingMode.CEILING).doubleValue();
                        double idf = Math.log(BigDecimal.valueOf(totalArticleLabels)
                                .divide(BigDecimal.valueOf(labelWithCountMap.get(labelPerArticle.getLabel())), 5, RoundingMode.CEILING).doubleValue());
                        labelPerArticle.setTf(tf);
                        labelPerArticle.setIdf(idf);
                    }
                    articleLabelFrequencyService.updateOne(articleLabelFrequency).orElseThrow(() -> new Exception("Can not update article label"));
                } catch (Exception ex) {
                    log.warn("Calculate TF-IDF error with ArticleAlgorithmLable id: {}", articleLabelFrequency.getId().toString());
                }

            }
            log.info("=== end page: {}", i + 1);
        }
        return Optional.of(Boolean.TRUE);
    }

    @Override
    public Optional<Boolean> simulateAvgTfIdf() throws Exception {
        List<ArticleLabelFrequency> articleLabelFrequencies = articleLabelFrequencyService.getMany(CommandQueryArticleLabelFrequency.builder()
                .isDescCreatedDate(true)
                .page(1)
                .size(99999)
                .build());
        if (CollectionUtils.isEmpty(articleLabelFrequencies))
            throw new Exception("Article label is empty");
        double eligibleRate = 0.05D;
        Map<String, Map<String, Double>> avgTfIdfByArticleLabel = new HashMap<>();
        for (ArticleLabelFrequency articleLabelFrequency : articleLabelFrequencies) {
            if (articleLabelFrequency.getLabels().get(0).getTf() == null)
                continue;
            Map<String, Double> eligibleArticleLabel = new HashMap<>();
            for (ArticleLabelFrequency.LabelPerArticle labelPerArticle : articleLabelFrequency.getLabels()) {
                double rate = BigDecimal.valueOf(labelPerArticle.getTf())
                        .multiply(BigDecimal.valueOf(labelPerArticle.getIdf()))
                        .setScale(20, RoundingMode.CEILING).doubleValue();
                if (rate > eligibleRate)
                    eligibleArticleLabel.put(labelPerArticle.getLabel(), rate);
            }
            if (MapUtils.isNotEmpty(eligibleArticleLabel))
                avgTfIdfByArticleLabel.put(articleLabelFrequency.getId().toString(), eligibleArticleLabel);
        }
        CSVExporter.exportTfIdfRate(avgTfIdfByArticleLabel);
        return Optional.of(Boolean.TRUE);
    }
}
