package thesis.core.article.model.label.org_label.service;

import thesis.core.article.command.CommandCommonQuery;
import thesis.core.article.model.label.org_label.ORGLabel;

import java.util.List;
import java.util.Optional;

public interface ORGLabelService {
    List<ORGLabel> getMany(CommandCommonQuery command);

    Optional<Boolean> addMany(List<ORGLabel> labels);
}
