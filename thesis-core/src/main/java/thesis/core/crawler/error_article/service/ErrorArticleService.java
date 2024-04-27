package thesis.core.crawler.error_article.service;

import thesis.core.crawler.error_article.ErrorArticle;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ErrorArticleService {
    List<ErrorArticle> findByUrls(List<String> urls);

    Optional<ErrorArticle> getOne(Map<String, Object> query);

    Optional<Boolean> add(ErrorArticle errorArticle);

    Optional<Boolean> addIfNotExist(ErrorArticle errorArticle);
}
