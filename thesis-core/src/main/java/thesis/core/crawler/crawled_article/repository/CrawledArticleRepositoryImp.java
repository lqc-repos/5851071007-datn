package thesis.core.crawler.crawled_article.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import thesis.common.mongo.operator.MongoDBOperatorImp;
import thesis.common.mongo.repo.MongoDBRepositoryImp;
import thesis.core.crawler.crawled_article.CrawledArticle;

@Repository
public class CrawledArticleRepositoryImp extends MongoDBRepositoryImp<CrawledArticle> implements CrawledArticleRepository {
    private final MongoDBOperatorImp<CrawledArticle> mongoDBOperator;

    public CrawledArticleRepositoryImp(@Value("${thesis.mongo.url}") String databaseUrl,
                                       @Value("${thesis.mongo.database-name}") String databaseName) {
        this.mongoDBOperator = new MongoDBOperatorImp<>(databaseUrl, databaseName, "crawled_article", CrawledArticle.class);
    }

    @Override
    public MongoDBOperatorImp<CrawledArticle> getMongoDBOperation() {
        return mongoDBOperator;
    }

}