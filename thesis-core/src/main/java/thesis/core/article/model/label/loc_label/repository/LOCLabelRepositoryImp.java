package thesis.core.article.model.label.loc_label.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import thesis.core.article.model.label.loc_label.LOCLabel;
import thesis.utils.mongo.operator.MongoDBOperatorImp;
import thesis.utils.mongo.repo.MongoDBRepositoryImp;

@Repository
public class LOCLabelRepositoryImp extends MongoDBRepositoryImp<LOCLabel> implements LOCLabelRepository {
    private final MongoDBOperatorImp<LOCLabel> mongoDBOperator;

    public LOCLabelRepositoryImp(@Value("${thesis.mongo.url}") String databaseUrl,
                                 @Value("${thesis.mongo.database-name}") String databaseName) {
        this.mongoDBOperator = new MongoDBOperatorImp<>(databaseUrl, databaseName, "loc_label", LOCLabel.class);
    }

    @Override
    public MongoDBOperatorImp<LOCLabel> getMongoDBOperation() {
        return mongoDBOperator;
    }

}