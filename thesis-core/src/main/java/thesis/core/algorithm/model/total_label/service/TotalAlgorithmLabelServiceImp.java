package thesis.core.algorithm.model.total_label.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.algorithm.model.total_label.TotalAlgorithmLabel;
import thesis.core.algorithm.model.total_label.repository.TotalAlgorithmLabelRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class TotalAlgorithmLabelServiceImp implements TotalAlgorithmLabelService {
    @Autowired
    private TotalAlgorithmLabelRepository totalAlgorithmLabelRepository;

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
    public Optional<Boolean> increase(String label) {
        Map<String, Object> query = new HashMap<>();
        query.put("label", label);
        Map<String, Object> update = new HashMap<>();
        update.put("$inc", new Document("count", 1));
        return totalAlgorithmLabelRepository.update(query, update);
    }

}
