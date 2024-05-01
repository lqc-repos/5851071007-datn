package thesis.core.article.model.label.custom_label.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import thesis.core.article.model.label.custom_label.CustomLabel;
import thesis.utils.mongo.operator.MongoDBOperatorImp;
import thesis.utils.mongo.repo.MongoDBRepositoryImp;

@Repository
public class CustomLabelRepositoryImp extends MongoDBRepositoryImp<CustomLabel> implements CustomLabelRepository {
    private final MongoDBOperatorImp<CustomLabel> mongoDBOperator;

    public CustomLabelRepositoryImp(@Value("${thesis.mongo.url}") String databaseUrl,
                                    @Value("${thesis.mongo.database-name}") String databaseName) {
        this.mongoDBOperator = new MongoDBOperatorImp<>(databaseUrl, databaseName, "custom_label", CustomLabel.class);
    }

    @Override
    public MongoDBOperatorImp<CustomLabel> getMongoDBOperation() {
        return mongoDBOperator;
    }

}