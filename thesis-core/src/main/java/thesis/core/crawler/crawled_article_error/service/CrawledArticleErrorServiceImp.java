package thesis.core.crawler.crawled_article_error.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.crawler.crawled_article_error.CrawledArticleError;
import thesis.core.crawler.crawled_article_error.repository.CrawledArticleErrorRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CrawledArticleErrorServiceImp implements CrawledArticleErrorService {
    @Autowired
    private CrawledArticleErrorRepository crawledArticleErrorRepository;

    @Override
    public List<CrawledArticleError> getByUrls(List<String> urls) {
        Map<String, Object> query = new HashMap<>();
        query.put("url", new org.bson.Document("$in", urls));
        return crawledArticleErrorRepository.findAll(query, new Document(), new Document());
    }

    @Override
    public Optional<CrawledArticleError> getOne(Map<String, Object> query) {
        return crawledArticleErrorRepository.findOne(query, new Document());
    }

    @Override
    public Optional<Boolean> add(CrawledArticleError crawledArticleError) {
        return crawledArticleErrorRepository.insert(crawledArticleError);
    }

    @Override
    public Optional<Boolean> addIfNotExist(CrawledArticleError crawledArticleError) {
        Optional<CrawledArticleError> existErrorArticle = crawledArticleErrorRepository
                .findOne(new Document("url", crawledArticleError.getUrl()), new Document());
        if (existErrorArticle.isPresent())
            return Optional.of(false);
        return add(crawledArticleError);
    }

}
