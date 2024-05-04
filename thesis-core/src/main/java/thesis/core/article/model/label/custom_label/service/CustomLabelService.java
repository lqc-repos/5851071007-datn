package thesis.core.article.model.label.custom_label.service;

import thesis.core.article.command.CommandCommonQuery;
import thesis.core.article.model.label.custom_label.CustomLabel;

import java.util.List;
import java.util.Optional;

public interface CustomLabelService {
    List<CustomLabel> getMany(CommandCommonQuery command);

    Optional<Boolean> addMany(List<CustomLabel> customLabels);
}
