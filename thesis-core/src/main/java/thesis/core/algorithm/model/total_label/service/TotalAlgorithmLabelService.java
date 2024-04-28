package thesis.core.algorithm.model.total_label.service;

import thesis.core.algorithm.model.total_label.TotalAlgorithmLabel;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface TotalAlgorithmLabelService {
    Set<String> getExistedLabel();

    Optional<TotalAlgorithmLabel> getOne(String label);

    Optional<Boolean> add(TotalAlgorithmLabel totalAlgorithmLabel);

    Optional<Boolean> increase(Set<String> existedLabel, Map<String, Long> countByLabel);
}
