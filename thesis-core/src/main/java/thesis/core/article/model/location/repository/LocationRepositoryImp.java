package thesis.core.article.model.location.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import thesis.core.article.model.location.Location;
import thesis.utils.mongo.operator.MongoDBOperatorImp;
import thesis.utils.mongo.repo.MongoDBRepositoryImp;

@Repository
public class LocationRepositoryImp extends MongoDBRepositoryImp<Location> implements LocationRepository {
    private final MongoDBOperatorImp<Location> mongoDBOperator;

    public LocationRepositoryImp(@Value("${thesis.mongo.url}") String databaseUrl,
                                 @Value("${thesis.mongo.database-name}") String databaseName) {
        this.mongoDBOperator = new MongoDBOperatorImp<>(databaseUrl, databaseName, "location", Location.class);
    }

    @Override
    public MongoDBOperatorImp<Location> getMongoDBOperation() {
        return mongoDBOperator;
    }

}