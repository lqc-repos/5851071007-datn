package thesis.core.article.service;

import thesis.core.article.Article;
import thesis.core.article.command.CommandCommonQuery;

import java.util.List;
import java.util.Optional;

public interface ArticleService {
    Optional<Long> count(CommandCommonQuery command);

    List<Article> get(CommandCommonQuery command);

    Optional<Boolean> add(Article article);

    Optional<Boolean> addMany(List<Article> articles);
}
