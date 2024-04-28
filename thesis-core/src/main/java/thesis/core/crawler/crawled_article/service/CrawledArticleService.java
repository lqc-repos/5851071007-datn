package thesis.core.crawler.crawled_article.service;

import thesis.core.crawler.crawled_article.CrawledArticle;

import java.util.List;
import java.util.Optional;

public interface CrawledArticleService {
    List<CrawledArticle> getByUrls(List<String> urls);

    Optional<Boolean> add(CrawledArticle crawledArticle);

    Optional<Boolean> addMany(List<CrawledArticle> crawledArticles);
}
