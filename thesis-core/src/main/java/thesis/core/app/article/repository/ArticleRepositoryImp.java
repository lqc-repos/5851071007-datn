package thesis.core.app.article.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import thesis.core.app.article.Article;
import thesis.utils.mongo.operator.MongoDBOperatorImp;
import thesis.utils.mongo.repo.MongoDBRepositoryImp;

@Repository
public class ArticleRepositoryImp extends MongoDBRepositoryImp<Article> implements ArticleRepository {
    private final MongoDBOperatorImp<Article> mongoDBOperator;

    public ArticleRepositoryImp(@Value("${thesis.mongo.url}") String databaseUrl,
                                @Value("${thesis.mongo.database-name}") String databaseName) {
        this.mongoDBOperator = new MongoDBOperatorImp<>(databaseUrl, databaseName, "article", Article.class);
    }

    @Override
    public MongoDBOperatorImp<Article> getMongoDBOperation() {
        return mongoDBOperator;
    }

}