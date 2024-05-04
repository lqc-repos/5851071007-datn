package thesis.core.article.model.label.loc_label.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.article.command.CommandCommonQuery;
import thesis.core.article.model.label.loc_label.LOCLabel;
import thesis.core.article.model.label.loc_label.repository.LOCLabelRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LOCLabelServiceImp implements LOCLabelService {
    @Autowired
    private LOCLabelRepository locLabelRepository;

    @Override
    public List<LOCLabel> getMany(CommandCommonQuery command) {
        Map<String, Object> sort = new HashMap<>();
        if (command.getIsDescCreatedDate() != null)
            sort.put("createdDate", command.getIsDescCreatedDate() ? -1 : 1);
        return locLabelRepository.find(new Document(), sort, (command.getPage() - 1) * command.getSize(), command.getSize());
    }

    @Override
    public Optional<Boolean> addMany(List<LOCLabel> labels) {
        return locLabelRepository.insertMany(labels);
    }
}
