package thesis.core.label_handler.model.total_label_frequency.service;

import thesis.core.label_handler.model.total_label_frequency.TotalLabelFrequency;
import thesis.core.label_handler.model.total_label_frequency.command.CommandQueryTotalLabelFrequency;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface TotalLabelFrequencyService {
    Set<String> getExistedLabel();

    List<TotalLabelFrequency> get(CommandQueryTotalLabelFrequency command);

    Optional<TotalLabelFrequency> getOne(String label);

    Optional<Boolean> add(TotalLabelFrequency totalLabelFrequency);

    Optional<Boolean> increase(Set<String> existedLabel, Map<String, Long> countByLabel);
}
