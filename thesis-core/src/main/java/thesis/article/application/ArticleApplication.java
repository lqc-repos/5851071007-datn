package thesis.article.application;

import thesis.article.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleApplication {
    List<Article> findByUrls(List<String> urls);

    Optional<Boolean> add(Article article);

    Optional<Boolean> addMany(List<Article> articles);
}
