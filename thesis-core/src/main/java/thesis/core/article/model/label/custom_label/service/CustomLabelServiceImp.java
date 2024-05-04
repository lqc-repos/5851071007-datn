package thesis.core.article.model.label.custom_label.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.article.command.CommandCommonQuery;
import thesis.core.article.model.label.custom_label.CustomLabel;
import thesis.core.article.model.label.custom_label.repository.CustomLabelRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomLabelServiceImp implements CustomLabelService {
    @Autowired
    private CustomLabelRepository customLabelRepository;

    @Override
    public List<CustomLabel> getMany(CommandCommonQuery command) {
        Map<String, Object> sort = new HashMap<>();
        if (command.getIsDescCreatedDate() != null)
            sort.put("createdDate", command.getIsDescCreatedDate() ? -1 : 1);
        return customLabelRepository.find(new Document(), sort, (command.getPage() - 1) * command.getSize(), command.getSize());
    }

    @Override
    public Optional<Boolean> addMany(List<CustomLabel> customLabels) {
        return customLabelRepository.insertMany(customLabels);
    }
}
