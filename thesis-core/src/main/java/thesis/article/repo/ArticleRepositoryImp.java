package thesis.article.repo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import thesis.article.Article;
import thesis.common.mongo.operator.MongoDBOperatorImp;
import thesis.common.mongo.repo.MongoDBRepositoryImp;

@Component
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