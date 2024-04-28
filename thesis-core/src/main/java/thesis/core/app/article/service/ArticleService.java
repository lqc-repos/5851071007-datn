package thesis.core.app.article.service;

import thesis.core.app.article.Article;
import thesis.core.app.article.command.CommandQueryArticle;

import java.util.List;
import java.util.Optional;

public interface ArticleService {
    Optional<Long> count(CommandQueryArticle command);

    List<Article> getMany(CommandQueryArticle command);

    Optional<Boolean> add(Article article);

    Optional<Boolean> addMany(List<Article> articles);
}
