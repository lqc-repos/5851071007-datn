package thesis.crawled_article.application;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import thesis.crawled_article.CrawledArticle;
import thesis.crawled_article.repo.CrawledArticleRepositoryImp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class CrawledArticleApplicationImp implements CrawledArticleApplication {
    @Autowired
    private CrawledArticleRepositoryImp errorArticleRepository;

    @Override
    public List<CrawledArticle> findByUrls(List<String> urls) {
        Map<String, Object> query = new HashMap<>();
        query.put("url", new Document("$in", urls));
        return errorArticleRepository.findAll(query, new Document(), new Document());
    }

    @Override
    public Optional<CrawledArticle> getOne(Map<String, Object> query) {
        return errorArticleRepository.findOne(query, new Document());
    }

    @Override
    public Optional<Boolean> add(CrawledArticle crawledArticle) {
        return errorArticleRepository.insert(crawledArticle);
    }
}
