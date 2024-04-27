package thesis.core.crawler.crawled_article.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.crawler.crawled_article.CrawledArticle;
import thesis.core.crawler.crawled_article.repository.CrawledArticleRepositoryImp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CrawledArticleServiceImp implements CrawledArticleService {
    @Autowired
    private CrawledArticleRepositoryImp articleRepository;

    @Override
    public List<CrawledArticle> findByUrls(List<String> urls) {
        Map<String, Object> query = new HashMap<>();
        query.put("url", new org.bson.Document("$in", urls));
        return articleRepository.findAll(query, new Document(), new Document());
    }

    @Override
    public Optional<Boolean> add(CrawledArticle crawledArticle) {
        return articleRepository.insert(crawledArticle);
    }

    @Override
    public Optional<Boolean> addMany(List<CrawledArticle> crawledArticles) {
        return articleRepository.insertMany(crawledArticles);
    }
}