package thesis.core.article.model.label.nlp_label.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.article.command.CommandCommonQuery;
import thesis.core.article.model.label.nlp_label.NLPLabel;
import thesis.core.article.model.label.nlp_label.repository.NLPLabelRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class NLPLabelServiceImp implements NLPLabelService {
    @Autowired
    private NLPLabelRepository nlpLabelRepository;

    @Override
    public List<NLPLabel> getMany(CommandCommonQuery command) {
        Map<String, Object> sort = new HashMap<>();
        if (command.getIsDescCreatedDate() != null)
            sort.put("createdDate", command.getIsDescCreatedDate() ? -1 : 1);
        return nlpLabelRepository.find(new Document(), sort, (command.getPage() - 1) * command.getSize(), command.getSize());
    }

    @Override
    public Optional<Boolean> addMany(List<NLPLabel> labels) {
        return nlpLabelRepository.insertMany(labels);
    }
}
