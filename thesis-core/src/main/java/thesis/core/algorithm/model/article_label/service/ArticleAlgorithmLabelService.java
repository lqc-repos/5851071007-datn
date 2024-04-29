package thesis.core.algorithm.model.article_label.service;

import thesis.core.algorithm.model.article_label.ArticleAlgorithmLabel;
import thesis.core.algorithm.model.article_label.command.CommandQueryArticleLabel;

import java.util.List;
import java.util.Optional;

public interface ArticleAlgorithmLabelService {
    Optional<Long> count();

    Optional<ArticleAlgorithmLabel> getOne(String label);

    List<ArticleAlgorithmLabel> getMany(CommandQueryArticleLabel command);

    Optional<Boolean> add(ArticleAlgorithmLabel articleAlgorithmLabel);

    Optional<Boolean> addMany(List<ArticleAlgorithmLabel> articleAlgorithmLabels);

    Optional<Boolean> addIfNotExist(ArticleAlgorithmLabel articleAlgorithmLabel);

    Optional<Boolean> updateOne(ArticleAlgorithmLabel articleAlgorithmLabel);
}
