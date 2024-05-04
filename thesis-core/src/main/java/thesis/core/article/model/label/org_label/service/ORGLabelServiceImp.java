package thesis.core.article.model.label.org_label.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.article.command.CommandCommonQuery;
import thesis.core.article.model.label.org_label.ORGLabel;
import thesis.core.article.model.label.org_label.repository.ORGLabelRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ORGLabelServiceImp implements ORGLabelService {
    @Autowired
    private ORGLabelRepository orgLabelRepository;

    @Override
    public List<ORGLabel> getMany(CommandCommonQuery command) {
        Map<String, Object> sort = new HashMap<>();
        if (command.getIsDescCreatedDate() != null)
            sort.put("createdDate", command.getIsDescCreatedDate() ? -1 : 1);
        return orgLabelRepository.find(new Document(), sort, (command.getPage() - 1) * command.getSize(), command.getSize());
    }

    @Override
    public Optional<Boolean> addMany(List<ORGLabel> labels) {
        return orgLabelRepository.insertMany(labels);
    }

}
