package thesis.core.crawler.error_article.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.crawler.error_article.ErrorArticle;
import thesis.core.crawler.error_article.repository.ErrorArticleRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ErrorArticleServiceImp implements ErrorArticleService {
    @Autowired
    private ErrorArticleRepository errorArticleRepository;

    @Override
    public List<ErrorArticle> getByUrls(List<String> urls) {
        Map<String, Object> query = new HashMap<>();
        query.put("url", new org.bson.Document("$in", urls));
        return errorArticleRepository.findAll(query, new Document(), new Document());
    }

    @Override
    public Optional<ErrorArticle> getOne(Map<String, Object> query) {
        return errorArticleRepository.findOne(query, new Document());
    }

    @Override
    public Optional<Boolean> add(ErrorArticle errorArticle) {
        return errorArticleRepository.insert(errorArticle);
    }

    @Override
    public Optional<Boolean> addIfNotExist(ErrorArticle errorArticle) {
        Optional<ErrorArticle> existErrorArticle = errorArticleRepository
                .findOne(new Document("url", errorArticle.getUrl()), new Document());
        if (existErrorArticle.isPresent())
            return Optional.of(false);
        return add(errorArticle);
    }

}
