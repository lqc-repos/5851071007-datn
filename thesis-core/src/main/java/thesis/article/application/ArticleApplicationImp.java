package thesis.article.application;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import thesis.article.Article;
import thesis.article.repo.ArticleRepositoryImp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ArticleApplicationImp implements ArticleApplication {
    @Autowired
    private ArticleRepositoryImp articleRepository;

    @Override
    public List<Article> findByUrls(List<String> urls) {
        Map<String, Object> query = new HashMap<>();
        query.put("url", new org.bson.Document("$in", urls));
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
