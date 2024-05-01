package thesis.core.article.model.label.per_label.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import thesis.core.article.model.label.per_label.PERLabel;
import thesis.utils.mongo.operator.MongoDBOperatorImp;
import thesis.utils.mongo.repo.MongoDBRepositoryImp;

@Repository
public class PERLabelRepositoryImp extends MongoDBRepositoryImp<PERLabel> implements PERLabelRepository {
    private final MongoDBOperatorImp<PERLabel> mongoDBOperator;

    public PERLabelRepositoryImp(@Value("${thesis.mongo.url}") String databaseUrl,
                                 @Value("${thesis.mongo.database-name}") String databaseName) {
        this.mongoDBOperator = new MongoDBOperatorImp<>(databaseUrl, databaseName, "per_label", PERLabel.class);
    }

    @Override
    public MongoDBOperatorImp<PERLabel> getMongoDBOperation() {
        return mongoDBOperator;
    }

}