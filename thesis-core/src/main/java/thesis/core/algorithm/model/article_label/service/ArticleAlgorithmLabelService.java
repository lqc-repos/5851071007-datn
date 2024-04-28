package thesis.core.algorithm.model.article_label.service;

import thesis.core.algorithm.model.article_label.ArticleAlgorithmLabel;

import java.util.Optional;

public interface ArticleAlgorithmLabelService {
    Optional<ArticleAlgorithmLabel> getOne(String label);

    Optional<Boolean> add(ArticleAlgorithmLabel articleAlgorithmLabel);
    Optional<Boolean> addIfNotExist(ArticleAlgorithmLabel articleAlgorithmLabel);
}
