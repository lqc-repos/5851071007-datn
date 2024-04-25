package thesis.crawled_article.application;

import thesis.crawled_article.CrawledArticle;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CrawledArticleApplication {
    List<CrawledArticle> findByUrls(List<String> urls);

    Optional<CrawledArticle> getOne(Map<String, Object> query);

    Optional<Boolean> add(CrawledArticle crawledArticle);
}
