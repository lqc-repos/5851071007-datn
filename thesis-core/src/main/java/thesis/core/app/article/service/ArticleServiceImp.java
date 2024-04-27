package thesis.core.app.article.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.app.article.Article;
import thesis.core.app.article.repository.ArticleRepositoryImp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ArticleServiceImp implements ArticleService {
    @Autowired
    private ArticleRepositoryImp articleRepository;

    @Override
    public List<Article> findByUrls(List<String> urls) {
        Map<String, Object> query = new HashMap<>();
        query.put("url", new Document("$in", urls));
        return articleRepository.findAll(query, new Document(), new Document());
    }

    @Override
    public Optional<Boolean> add(Article article) {
        return articleRepository.insert(article);
    }

    @Override
    public Optional<Boolean> addMany(List<Article> articles) {
        return articleRepository.insertMany(articles);
    }
}
