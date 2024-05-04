package thesis.core.article.model.label.nlp_label.service;

import thesis.core.article.command.CommandCommonQuery;
import thesis.core.article.model.label.nlp_label.NLPLabel;

import java.util.List;
import java.util.Optional;

public interface NLPLabelService {
    List<NLPLabel> getMany(CommandCommonQuery command);

    Optional<Boolean> addMany(List<NLPLabel> labels);
}
