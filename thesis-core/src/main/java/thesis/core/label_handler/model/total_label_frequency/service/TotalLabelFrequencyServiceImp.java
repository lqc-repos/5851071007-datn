package thesis.core.label_handler.model.total_label_frequency.service;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.label_handler.model.total_label_frequency.TotalLabelFrequency;
import thesis.core.label_handler.model.total_label_frequency.command.CommandQueryTotalLabelFrequency;
import thesis.core.label_handler.model.total_label_frequency.repository.TotalLabelFrequencyRepository;

import java.util.*;

@Service
@Log4j2
public class TotalLabelFrequencyServiceImp implements TotalLabelFrequencyService {
    @Autowired
    private TotalLabelFrequencyRepository totalLabelFrequencyRepository;

    @Override
    public Set<String> getExistedLabel() {
        Map<String, Object> projection = new HashMap<>();
        projection.put("_id", 0);
        projection.put("label", 1);
        List<TotalLabelFrequency> totalLabelFrequencies = totalLabelFrequencyRepository.findAll(new Document(), new Document(), projection);
        return new HashSet<>(totalLabelFrequencies.stream().map(TotalLabelFrequency::getLabel).toList());
    }

    @Override
    public List<TotalLabelFrequency> get(CommandQueryTotalLabelFrequency command) {
        Map<String, Object> query = new HashMap<>();
        if (StringUtils.isNotBlank(command.getLabel()))
            query.put("label", command.getLabel());
        Map<String, Object> projection = new HashMap<>();
        if (BooleanUtils.isTrue(command.getHasProjection())) {
            projection.put("_id", BooleanUtils.isTrue(command.getTotalLabelProjection().getIsId()) ? 1 : 0);
            projection.put("label", BooleanUtils.isTrue(command.getTotalLabelProjection().getIsLabel()) ? 1 : 0);
            projection.put("count", BooleanUtils.isTrue(command.getTotalLabelProjection().getIsCount()) ? 1 : 0);
        }
        return totalLabelFrequencyRepository.findAll(query, new Document(), projection);
    }

    @Override
    public Optional<TotalLabelFrequency> getOne(String label) {
        Map<String, Object> query = new HashMap<>();
        query.put("label", label);
        return totalLabelFrequencyRepository.findOne(query, new Document());
    }

    @Override
    public Optional<Boolean> add(TotalLabelFrequency totalLabelFrequency) {
        return totalLabelFrequencyRepository.insert(totalLabelFrequency);
    }

    @Override
    public Optional<Boolean> increase(Set<String> existedLabel, Map<String, Long> countByLabel) {
        for (Map.Entry<String, Long> entry : countByLabel.entrySet()) {
            if (!existedLabel.contains(entry.getKey())) {
                if (BooleanUtils.isNotTrue(add(TotalLabelFrequency.builder().label(entry.getKey()).count(0L).build()).orElse(Boolean.FALSE)))
                    log.warn("Cannot new TotalAlgorithmLabel with label: {} and count: {}", entry.getKey(), entry.getValue());
                existedLabel.add(entry.getKey());
            }
            increase(entry.getKey(), entry.getValue());
        }
        return Optional.of(Boolean.TRUE);
    }

    private Optional<Boolean> increase(String label, Long count) {
        Map<String, Object> query = new HashMap<>();
        query.put("label", label);
        Map<String, Object> update = new HashMap<>();
        update.put("$inc", new Document("count", count));
        return totalLabelFrequencyRepository.increase(query, update);
    }

}
