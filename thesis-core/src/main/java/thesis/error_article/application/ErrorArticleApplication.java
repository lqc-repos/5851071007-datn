package thesis.error_article.application;

import thesis.error_article.ErrorArticle;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ErrorArticleApplication {
    List<ErrorArticle> findByUrls(List<String> urls);

    Optional<ErrorArticle> getOne(Map<String, Object> query);

    Optional<Boolean> add(ErrorArticle errorArticle);

    Optional<Boolean> addIfNotExist(ErrorArticle errorArticle);
}
