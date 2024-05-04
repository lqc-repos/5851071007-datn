package thesis.core.article.model.label.per_label.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.article.command.CommandCommonQuery;
import thesis.core.article.model.label.per_label.PERLabel;
import thesis.core.article.model.label.per_label.repository.PERLabelRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PERLabelServiceImp implements PERLabelService {
    @Autowired
    private PERLabelRepository perLabelRepository;

    @Override
    public List<PERLabel> getMany(CommandCommonQuery command) {
        Map<String, Object> sort = new HashMap<>();
        if (command.getIsDescCreatedDate() != null)
            sort.put("createdDate", command.getIsDescCreatedDate() ? -1 : 1);
        return perLabelRepository.find(new Document(), sort, (command.getPage() - 1) * command.getSize(), command.getSize());
    }

    @Override
    public Optional<Boolean> addMany(List<PERLabel> labels) {
        return perLabelRepository.insertMany(labels);
    }

}
