package thesis.core.crawler.crawled_article_error.service;

import thesis.core.crawler.crawled_article_error.CrawledArticleError;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CrawledArticleErrorService {
    List<CrawledArticleError> getByUrls(List<String> urls);

    Optional<CrawledArticleError> getOne(Map<String, Object> query);

    Optional<Boolean> add(CrawledArticleError crawledArticleError);

    Optional<Boolean> addIfNotExist(CrawledArticleError crawledArticleError);
}
