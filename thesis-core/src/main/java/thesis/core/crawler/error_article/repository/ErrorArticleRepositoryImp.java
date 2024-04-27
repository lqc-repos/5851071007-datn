package thesis.core.crawler.error_article.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import thesis.core.crawler.error_article.ErrorArticle;
import thesis.utils.mongo.operator.MongoDBOperatorImp;
import thesis.utils.mongo.repo.MongoDBRepositoryImp;

@Repository
public class ErrorArticleRepositoryImp extends MongoDBRepositoryImp<ErrorArticle> implements ErrorArticleRepository {
    private final MongoDBOperatorImp<ErrorArticle> mongoDBOperator;

    public ErrorArticleRepositoryImp(@Value("${thesis.mongo.url}") String databaseUrl,
                                     @Value("${thesis.mongo.database-name}") String databaseName) {
        this.mongoDBOperator = new MongoDBOperatorImp<>(databaseUrl, databaseName, "error_article", ErrorArticle.class);
    }

    @Override
    public MongoDBOperatorImp<ErrorArticle> getMongoDBOperation() {
        return mongoDBOperator;
    }

}