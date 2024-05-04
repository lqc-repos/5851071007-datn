package thesis.core.article.model.label.per_label.service;

import thesis.core.article.command.CommandCommonQuery;
import thesis.core.article.model.label.per_label.PERLabel;

import java.util.List;
import java.util.Optional;

public interface PERLabelService {
    List<PERLabel> getMany(CommandCommonQuery command);

    Optional<Boolean> addMany(List<PERLabel> labels);
}
