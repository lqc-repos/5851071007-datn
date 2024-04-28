package thesis.core.algorithm.model.article_label.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.algorithm.model.article_label.ArticleAlgorithmLabel;
import thesis.core.algorithm.model.article_label.repository.ArticleAlgorithmLabelRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ArticleAlgorithmLabelServiceImp implements ArticleAlgorithmLabelService {
    @Autowired
    private ArticleAlgorithmLabelRepository articleAlgorithmLabelRepository;

    @Override
    public Optional<ArticleAlgorithmLabel> getOne(String label) {
        Map<String, Object> query = new HashMap<>();
        query.put("label", label);
        return articleAlgorithmLabelRepository.findOne(query, new Document());
    }

    @Override
    public Optional<Boolean> add(ArticleAlgorithmLabel articleAlgorithmLabel) {
        return articleAlgorithmLabelRepository.insert(articleAlgorithmLabel);
    }

    @Override
    public Optional<Boolean> addIfNotExist(ArticleAlgorithmLabel articleAlgorithmLabel) {
        if (getOne(articleAlgorithmLabel.getLabel()).isPresent())
            return Optional.of(Boolean.FALSE);
        return articleAlgorithmLabelRepository.insert(articleAlgorithmLabel);
    }
}
