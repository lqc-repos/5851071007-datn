package thesis.error_article.application;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import thesis.error_article.ErrorArticle;
import thesis.error_article.repo.ErrorArticleRepositoryImp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ErrorArticleApplicationImp implements ErrorArticleApplication {
    @Autowired
    private ErrorArticleRepositoryImp errorArticleRepository;

    @Override
    public List<ErrorArticle> findByUrls(List<String> urls) {
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
