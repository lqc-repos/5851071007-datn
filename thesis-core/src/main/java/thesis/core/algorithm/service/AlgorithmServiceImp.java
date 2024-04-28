package thesis.core.algorithm.service;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.algorithm.model.article_label.ArticleAlgorithmLabel;
import thesis.core.algorithm.model.article_label.service.ArticleAlgorithmLabelService;
import thesis.core.algorithm.model.total_label.service.TotalAlgorithmLabelService;
import thesis.core.app.article.Article;
import thesis.core.app.article.command.CommandQueryArticle;
import thesis.core.app.article.service.ArticleService;
import thesis.core.crawler.crawled_article.CrawledArticle;
import thesis.core.crawler.crawled_article.command.CommandQueryCrawledArticle;
import thesis.core.crawler.crawled_article.service.CrawledArticleService;
import thesis.core.nlp.dto.AnnotatedWord;
import thesis.core.nlp.service.NLPService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class AlgorithmServiceImp implements AlgorithmService {
    @Autowired
    private CrawledArticleService crawledArticleService;
    @Autowired
    private ArticleAlgorithmLabelService articleAlgorithmLabelService;
    @Autowired
    private TotalAlgorithmLabelService totalAlgorithmLabelService;
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
                    .title(crawledArticle.getTitle())
                    .location(crawledArticle.getLocation())
                    .description(crawledArticle.getDescription())
                    .content(crawledArticle.getContent())
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
    public Optional<Boolean> storageFrequency() throws Exception {
        Set<String> existedTotalLabels = totalAlgorithmLabelService.getExistedLabel();
        Long totalArticle = articleService.count(CommandQueryArticle.builder().build()).orElseThrow();
        int sizePerPage = 50, totalPage = (int) ((totalArticle + sizePerPage - 1) / sizePerPage);
        Set<String> urls = new HashSet<>();
        for (int i = 0; i < totalPage; i++) {
            List<ArticleAlgorithmLabel> articleAlgorithmLabels = new ArrayList<>();
            Map<String, Long> totalByLabel = new HashMap<>();
            List<Article> articles = articleService.getMany(CommandQueryArticle.builder()
                    .isDescPublicationDate(true)
                    .isDescCreatedDate(true)
                    .page(i + 1)
                    .size(sizePerPage)
                    .build());
            for (Article article : articles) {
                if (urls.contains(article.getUrl()))
                    continue;
                String contentBuilder = article.getTitle() + "\n" +
                        article.getDescription() + "\n" +
                        article.getContent() + "\n";
                Optional<AnnotatedWord> annotatedWordOptional = nlpService.annotate(contentBuilder);
                if (annotatedWordOptional.isEmpty()) {
                    log.warn("Cannot annotate articleId {}", article.getId().toString());
                    continue;
                }
                AnnotatedWord annotatedWord = annotatedWordOptional.get();
                Map<String, Long> countByLabelSorted = annotatedWord.getTaggedWords().stream()
                        .collect(Collectors.collectingAndThen(
                                Collectors.groupingBy(AnnotatedWord.TaggedWord::getWord, Collectors.counting()),
                                map -> map.entrySet().stream()
                                        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                                (oldValue, newValue) -> oldValue, LinkedHashMap::new))
                        ));
                countByLabelSorted.keySet().forEach(key -> {
                    if (!totalByLabel.containsKey(key))
                        totalByLabel.put(key, 1L);
                    else
                        totalByLabel.put(key, totalByLabel.get(key) + 1);
                });
                long totalLabel = countByLabelSorted.values().stream().mapToLong(m -> m).sum();
                ArticleAlgorithmLabel articleAlgorithmLabel = ArticleAlgorithmLabel.builder()
                        .articleId(article.getId().toString())
                        .totalLabel(totalLabel)
                        .labels(countByLabelSorted.entrySet().stream()
                                .map(k -> ArticleAlgorithmLabel.LabelPerArticle.builder()
                                        .label(k.getKey())
                                        .count(k.getValue())
                                        .build())
                                .collect(Collectors.toList()))
                        .build();
                articleAlgorithmLabels.add(articleAlgorithmLabel);
                urls.add(article.getUrl());
            }
            articleAlgorithmLabelService.addMany(articleAlgorithmLabels);
            totalAlgorithmLabelService.increase(existedTotalLabels, totalByLabel);
        }
        return Optional.of(Boolean.TRUE);
    }
}
