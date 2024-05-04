package thesis.core.label_handler.model.article_label_frequency.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.label_handler.model.article_label_frequency.ArticleLabelFrequency;
import thesis.core.label_handler.model.article_label_frequency.command.CommandQueryArticleLabelFrequency;
import thesis.core.label_handler.model.article_label_frequency.repository.ArticleLabelFrequencyRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ArticleLabelFrequencyServiceImp implements ArticleLabelFrequencyService {
    @Autowired
    private ArticleLabelFrequencyRepository articleLabelFrequencyRepository;

    @Override
    public Optional<Long> count() {
        return articleLabelFrequencyRepository.count(new Document());
    }

    @Override
    public Optional<ArticleLabelFrequency> getOne(String label) {
        Map<String, Object> query = new HashMap<>();
        query.put("label", label);
        return articleLabelFrequencyRepository.findOne(query, new Document());
    }

    @Override
    public List<ArticleLabelFrequency> getMany(CommandQueryArticleLabelFrequency command) {
        Map<String, Object> sort = new HashMap<>();
        if (command.getIsDescId() != null)
            sort.put("_id", command.getIsDescId() ? -1 : 1);
        if (command.getIsDescCreatedDate() != null)
            sort.put("createdDate", command.getIsDescCreatedDate() ? -1 : 1);
        return articleLabelFrequencyRepository.find(new Document(), sort, (command.getPage() - 1) * command.getSize(), command.getSize());
    }

    @Override
    public Optional<Boolean> add(ArticleLabelFrequency articleLabelFrequency) {
        return articleLabelFrequencyRepository.insert(articleLabelFrequency);
    }

    @Override
    public Optional<Boolean> addMany(List<ArticleLabelFrequency> articleLabelFrequencies) {
        return articleLabelFrequencyRepository.insertMany(articleLabelFrequencies);
    }

    @Override
    public Optional<Boolean> addIfNotExist(ArticleLabelFrequency articleLabelFrequency) {
        if (getOne(articleLabelFrequency.getArticleId()).isPresent())
            return Optional.of(Boolean.FALSE);
        return articleLabelFrequencyRepository.insert(articleLabelFrequency);
    }

    @Override
    public Optional<Boolean> updateOne(ArticleLabelFrequency articleLabelFrequency) {
        Map<String, Object> query = new HashMap<>();
        query.put("_id", articleLabelFrequency.getId());
        Map<String, Object> update = new HashMap<>();
        update.put("labels", articleLabelFrequency.getLabels());
        return articleLabelFrequencyRepository.update(query, update);
    }
}
