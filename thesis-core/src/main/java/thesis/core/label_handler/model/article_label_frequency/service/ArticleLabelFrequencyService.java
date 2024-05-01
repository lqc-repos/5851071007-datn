package thesis.core.label_handler.model.article_label_frequency.service;

import thesis.core.label_handler.model.article_label_frequency.ArticleLabelFrequency;
import thesis.core.label_handler.model.article_label_frequency.command.CommandQueryArticleLabelFrequency;

import java.util.List;
import java.util.Optional;

public interface ArticleLabelFrequencyService {
    Optional<Long> count();

    Optional<ArticleLabelFrequency> getOne(String label);

    List<ArticleLabelFrequency> getMany(CommandQueryArticleLabelFrequency command);

    Optional<Boolean> add(ArticleLabelFrequency articleLabelFrequency);

    Optional<Boolean> addMany(List<ArticleLabelFrequency> articleLabelFrequencies);

    Optional<Boolean> addIfNotExist(ArticleLabelFrequency articleLabelFrequency);

    Optional<Boolean> updateOne(ArticleLabelFrequency articleLabelFrequency);
}
