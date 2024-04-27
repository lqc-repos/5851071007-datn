package thesis.core.crawler.crawled_article_log.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import thesis.utils.mongo.operator.MongoDBOperatorImp;
import thesis.utils.mongo.repo.MongoDBRepositoryImp;
import thesis.core.crawler.crawled_article_log.CrawledArticleLog;

@Service
public class CrawledArticleLogRepositoryImp extends MongoDBRepositoryImp<CrawledArticleLog> implements CrawledArticleLogRepository {
    private final MongoDBOperatorImp<CrawledArticleLog> mongoDBOperator;

    public CrawledArticleLogRepositoryImp(@Value("${thesis.mongo.url}") String databaseUrl,
                                          @Value("${thesis.mongo.database-name}") String databaseName) {
        this.mongoDBOperator = new MongoDBOperatorImp<>(databaseUrl, databaseName, "crawled_article_log", CrawledArticleLog.class);
    }

    @Override
    public MongoDBOperatorImp<CrawledArticleLog> getMongoDBOperation() {
        return mongoDBOperator;
    }

}