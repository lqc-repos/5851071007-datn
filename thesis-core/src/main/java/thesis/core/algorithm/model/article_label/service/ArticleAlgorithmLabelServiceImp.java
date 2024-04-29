package thesis.core.algorithm.model.article_label.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.algorithm.model.article_label.ArticleAlgorithmLabel;
import thesis.core.algorithm.model.article_label.command.CommandQueryArticleLabel;
import thesis.core.algorithm.model.article_label.repository.ArticleAlgorithmLabelRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ArticleAlgorithmLabelServiceImp implements ArticleAlgorithmLabelService {
    @Autowired
    private ArticleAlgorithmLabelRepository articleAlgorithmLabelRepository;

    @Override
    public Optional<Long> count() {
        return articleAlgorithmLabelRepository.count(new Document());
    }

    @Override
    public Optional<ArticleAlgorithmLabel> getOne(String label) {
        Map<String, Object> query = new HashMap<>();
        query.put("label", label);
        return articleAlgorithmLabelRepository.findOne(query, new Document());
    }

    @Override
    public List<ArticleAlgorithmLabel> getMany(CommandQueryArticleLabel command) {
        Map<String, Object> sort = new HashMap<>();
        if (command.getIsDescCreatedDate() != null)
            sort.put("createdDate", command.getIsDescCreatedDate() ? -1 : 1);
        return articleAlgorithmLabelRepository.find(new Document(), sort, (command.getPage() - 1) * command.getSize(), command.getSize());
    }

    @Override
    public Optional<Boolean> add(ArticleAlgorithmLabel articleAlgorithmLabel) {
        return articleAlgorithmLabelRepository.insert(articleAlgorithmLabel);
    }

    @Override
    public Optional<Boolean> addMany(List<ArticleAlgorithmLabel> articleAlgorithmLabels) {
        return articleAlgorithmLabelRepository.insertMany(articleAlgorithmLabels);
    }

    @Override
    public Optional<Boolean> addIfNotExist(ArticleAlgorithmLabel articleAlgorithmLabel) {
        if (getOne(articleAlgorithmLabel.getArticleId()).isPresent())
            return Optional.of(Boolean.FALSE);
        return articleAlgorithmLabelRepository.insert(articleAlgorithmLabel);
    }

    @Override
    public Optional<Boolean> updateOne(ArticleAlgorithmLabel articleAlgorithmLabel) {
        Map<String, Object> query = new HashMap<>();
        query.put("_id", articleAlgorithmLabel.getId());
        Map<String, Object> update = new HashMap<>();
        update.put("labels", articleAlgorithmLabel.getLabels());
        return articleAlgorithmLabelRepository.update(query, update);
    }
}
