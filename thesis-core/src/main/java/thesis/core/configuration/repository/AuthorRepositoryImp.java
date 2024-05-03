package thesis.core.configuration.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import thesis.core.configuration.ThesisConfiguration;
import thesis.utils.mongo.operator.MongoDBOperatorImp;
import thesis.utils.mongo.repo.MongoDBRepositoryImp;

@Repository
public class AuthorRepositoryImp extends MongoDBRepositoryImp<ThesisConfiguration> implements AuthorRepository {
    private final MongoDBOperatorImp<ThesisConfiguration> mongoDBOperator;

    public AuthorRepositoryImp(@Value("${thesis.mongo.url}") String databaseUrl,
                               @Value("${thesis.mongo.database-name}") String databaseName) {
        this.mongoDBOperator = new MongoDBOperatorImp<>(databaseUrl, databaseName, "author", ThesisConfiguration.class);
    }

    @Override
    public MongoDBOperatorImp<ThesisConfiguration> getMongoDBOperation() {
        return mongoDBOperator;
    }

}