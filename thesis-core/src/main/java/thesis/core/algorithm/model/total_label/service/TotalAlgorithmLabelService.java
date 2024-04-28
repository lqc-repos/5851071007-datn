package thesis.core.algorithm.model.total_label.service;

import thesis.core.algorithm.model.total_label.TotalAlgorithmLabel;

import java.util.Optional;

public interface TotalAlgorithmLabelService {
    Optional<TotalAlgorithmLabel> getOne(String label);

    Optional<Boolean> add(TotalAlgorithmLabel totalAlgorithmLabel);

    Optional<Boolean> increase(String label);
}
