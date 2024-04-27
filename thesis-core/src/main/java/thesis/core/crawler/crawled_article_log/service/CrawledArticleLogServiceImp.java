package thesis.core.crawler.crawled_article_log.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import thesis.core.crawler.crawled_article_log.CrawledArticleLog;
import thesis.core.crawler.crawled_article_log.repository.CrawledArticleLogRepositoryImp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class CrawledArticleLogServiceImp implements CrawledArticleLogService {
    @Autowired
    private CrawledArticleLogRepositoryImp errorArticleRepository;

    @Override
    public List<CrawledArticleLog> findByUrls(List<String> urls) {
        Map<String, Object> query = new HashMap<>();
        query.put("url", new Document("$in", urls));
        return errorArticleRepository.findAll(query, new Document(), new Document());
    }

    @Override
    public Optional<CrawledArticleLog> getOne(Map<String, Object> query) {
        return errorArticleRepository.findOne(query, new Document());
    }

    @Override
    public Optional<Boolean> add(CrawledArticleLog crawledArticleLog) {
        return errorArticleRepository.insert(crawledArticleLog);
    }
}
