package thesis.core.app.author.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import thesis.core.app.article.Article;
import thesis.utils.mongo.operator.MongoDBOperatorImp;
import thesis.utils.mongo.repo.MongoDBRepositoryImp;

@Repository
public class AuthorRepositoryImp extends MongoDBRepositoryImp<Article> implements AuthorRepository {
    private final MongoDBOperatorImp<Article> mongoDBOperator;

    public AuthorRepositoryImp(@Value("${thesis.mongo.url}") String databaseUrl,
                               @Value("${thesis.mongo.database-name}") String databaseName) {
        this.mongoDBOperator = new MongoDBOperatorImp<>(databaseUrl, databaseName, "author", Article.class);
    }

    @Override
    public MongoDBOperatorImp<Article> getMongoDBOperation() {
        return mongoDBOperator;
    }

}