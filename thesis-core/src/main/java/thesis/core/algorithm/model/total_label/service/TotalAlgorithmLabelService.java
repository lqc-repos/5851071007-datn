package thesis.core.algorithm.model.total_label.service;

import thesis.core.algorithm.model.total_label.TotalAlgorithmLabel;
import thesis.core.algorithm.model.total_label.command.CommandQueryTotalLabel;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface TotalAlgorithmLabelService {
    Set<String> getExistedLabel();

    List<TotalAlgorithmLabel> get(CommandQueryTotalLabel command);

    Optional<TotalAlgorithmLabel> getOne(String label);

    Optional<Boolean> add(TotalAlgorithmLabel totalAlgorithmLabel);

    Optional<Boolean> increase(Set<String> existedLabel, Map<String, Long> countByLabel);
}
