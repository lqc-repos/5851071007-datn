package thesis.core.app.label.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import thesis.core.app.label.Label;
import thesis.utils.mongo.operator.MongoDBOperatorImp;
import thesis.utils.mongo.repo.MongoDBRepositoryImp;

@Repository
public class LabelRepositoryImp extends MongoDBRepositoryImp<Label> implements LabelRepository {
    private final MongoDBOperatorImp<Label> mongoDBOperator;

    public LabelRepositoryImp(@Value("${thesis.mongo.url}") String databaseUrl,
                              @Value("${thesis.mongo.database-name}") String databaseName) {
        this.mongoDBOperator = new MongoDBOperatorImp<>(databaseUrl, databaseName, "label", Label.class);
    }

    @Override
    public MongoDBOperatorImp<Label> getMongoDBOperation() {
        return mongoDBOperator;
    }

}