package thesis.core.article.model.label.loc_label.service;

import thesis.core.article.command.CommandCommonQuery;
import thesis.core.article.model.label.loc_label.LOCLabel;

import java.util.List;
import java.util.Optional;

public interface LOCLabelService {
    List<LOCLabel> getMany(CommandCommonQuery command);

    Optional<Boolean> addMany(List<LOCLabel> labels);
}
