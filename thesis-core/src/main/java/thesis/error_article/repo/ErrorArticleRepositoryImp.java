package thesis.error_article.repo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import thesis.common.mongo.operator.MongoDBOperatorImp;
import thesis.common.mongo.repo.MongoDBRepositoryImp;
import thesis.error_article.ErrorArticle;

@Component
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