package thesis.core.crawler.crawled_article.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.crawler.crawled_article.CrawledArticle;
import thesis.core.crawler.crawled_article.command.CommandQueryCrawledArticle;
import thesis.core.crawler.crawled_article.repository.CrawledArticleRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CrawledArticleServiceImp implements CrawledArticleService {
    @Autowired
    private CrawledArticleRepository crawledArticleRepository;

    @Override
    public Optional<Long> count(CommandQueryCrawledArticle command) {
        return crawledArticleRepository.count(new Document());
    }

    @Override
    public List<CrawledArticle> getMany(CommandQueryCrawledArticle command) {
        Map<String, Object> sort = new HashMap<>();
        if (command.getIsDescCreatedDate() != null)
            sort.put("createdDate", command.getIsDescCreatedDate() ? -1 : 1);
        if (command.getIsDescPublicationDate() != null)
            sort.put("publicationDate", command.getIsDescPublicationDate() ? -1 : 1);
        Map<String, Object> query = new HashMap<>();
        query.put("images", new Document("$exists", false));
        return crawledArticleRepository.find(query, sort, (command.getPage() - 1) * command.getSize(), command.getSize());
    }

    @Override
    public List<CrawledArticle> getByUrls(List<String> urls) {
        Map<String, Object> query = new HashMap<>();
        query.put("url", new org.bson.Document("$in", urls));
        return crawledArticleRepository.find(query, new Document(), new Document());
    }

    @Override
    public Optional<Boolean> add(CrawledArticle crawledArticle) {
        return crawledArticleRepository.insert(crawledArticle);
    }

    @Override
    public Optional<Boolean> addMany(List<CrawledArticle> crawledArticles) {
        return crawledArticleRepository.insertMany(crawledArticles);
    }
}
