package thesis.core.news.role.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import thesis.core.news.role.Role;
import thesis.utils.mongo.operator.MongoDBOperatorImp;
import thesis.utils.mongo.repo.MongoDBRepositoryImp;

@Repository
public class RoleRepositoryImp extends MongoDBRepositoryImp<Role> implements RoleRepository {
    private final MongoDBOperatorImp<Role> mongoDBOperator;

    public RoleRepositoryImp(@Value("${thesis.mongo.url}") String databaseUrl,
                             @Value("${thesis.mongo.database-name}") String databaseName) {
        this.mongoDBOperator = new MongoDBOperatorImp<>(databaseUrl, databaseName, "role", Role.class);
    }

    @Override
    public MongoDBOperatorImp<Role> getMongoDBOperation() {
        return mongoDBOperator;
    }

}