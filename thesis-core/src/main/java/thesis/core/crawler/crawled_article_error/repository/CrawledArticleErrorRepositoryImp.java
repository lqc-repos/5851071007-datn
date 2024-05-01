package thesis.core.crawler.crawled_article_error.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import thesis.core.crawler.crawled_article_error.CrawledArticleError;
import thesis.utils.mongo.operator.MongoDBOperatorImp;
import thesis.utils.mongo.repo.MongoDBRepositoryImp;

@Repository
public class CrawledArticleErrorRepositoryImp extends MongoDBRepositoryImp<CrawledArticleError> implements CrawledArticleErrorRepository {
    private final MongoDBOperatorImp<CrawledArticleError> mongoDBOperator;

    public CrawledArticleErrorRepositoryImp(@Value("${thesis.mongo.url}") String databaseUrl,
                                            @Value("${thesis.mongo.database-name}") String databaseName) {
        this.mongoDBOperator = new MongoDBOperatorImp<>(databaseUrl, databaseName, "crawled_article_error", CrawledArticleError.class);
    }

    @Override
    public MongoDBOperatorImp<CrawledArticleError> getMongoDBOperation() {
        return mongoDBOperator;
    }

}