package thesis.core.app.author.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import thesis.core.app.author.Author;
import thesis.utils.mongo.operator.MongoDBOperatorImp;
import thesis.utils.mongo.repo.MongoDBRepositoryImp;

@Repository
public class AuthorRepositoryImp extends MongoDBRepositoryImp<Author> implements AuthorRepository {
    private final MongoDBOperatorImp<Author> mongoDBOperator;

    public AuthorRepositoryImp(@Value("${thesis.mongo.url}") String databaseUrl,
                               @Value("${thesis.mongo.database-name}") String databaseName) {
        this.mongoDBOperator = new MongoDBOperatorImp<>(databaseUrl, databaseName, "author", Author.class);
    }

    @Override
    public MongoDBOperatorImp<Author> getMongoDBOperation() {
        return mongoDBOperator;
    }

}