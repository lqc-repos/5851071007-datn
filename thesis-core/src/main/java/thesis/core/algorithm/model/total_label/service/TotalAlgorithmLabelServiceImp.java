package thesis.core.algorithm.model.total_label.service;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.BooleanUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.algorithm.model.total_label.TotalAlgorithmLabel;
import thesis.core.algorithm.model.total_label.repository.TotalAlgorithmLabelRepository;

import java.util.*;

@Service
@Log4j2
public class TotalAlgorithmLabelServiceImp implements TotalAlgorithmLabelService {
    @Autowired
    private TotalAlgorithmLabelRepository totalAlgorithmLabelRepository;

    @Override
    public Set<String> getExistedLabel() {
        Map<String, Object> projection = new HashMap<>();
        projection.put("_id", 0);
        projection.put("label", 1);
        List<TotalAlgorithmLabel> totalAlgorithmLabels = totalAlgorithmLabelRepository.findAll(new Document(), new Document(), projection);
        return new HashSet<>(totalAlgorithmLabels.stream().map(TotalAlgorithmLabel::getLabel).toList());
    }

    @Override
    public Optional<TotalAlgorithmLabel> getOne(String label) {
        Map<String, Object> query = new HashMap<>();
        query.put("label", label);
        return totalAlgorithmLabelRepository.findOne(query, new Document());
    }

    @Override
    public Optional<Boolean> add(TotalAlgorithmLabel totalAlgorithmLabel) {
        return totalAlgorithmLabelRepository.insert(totalAlgorithmLabel);
    }

    @Override
    public Optional<Boolean> increase(Set<String> existedLabel, Map<String, Long> countByLabel) {
        for (Map.Entry<String, Long> entry : countByLabel.entrySet()) {
            if (!existedLabel.contains(entry.getKey())) {
                if (BooleanUtils.isNotTrue(add(TotalAlgorithmLabel.builder().label(entry.getKey()).count(0L).build()).orElse(Boolean.FALSE)))
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
        return totalAlgorithmLabelRepository.increase(query, update);
    }

}
