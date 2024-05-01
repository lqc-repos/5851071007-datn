package thesis.core.article.model.label.org_label.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import thesis.core.article.model.label.org_label.ORGLabel;
import thesis.utils.mongo.operator.MongoDBOperatorImp;
import thesis.utils.mongo.repo.MongoDBRepositoryImp;

@Repository
public class ORGLabelRepositoryImp extends MongoDBRepositoryImp<ORGLabel> implements ORGLabelRepository {
    private final MongoDBOperatorImp<ORGLabel> mongoDBOperator;

    public ORGLabelRepositoryImp(@Value("${thesis.mongo.url}") String databaseUrl,
                                 @Value("${thesis.mongo.database-name}") String databaseName) {
        this.mongoDBOperator = new MongoDBOperatorImp<>(databaseUrl, databaseName, "org_label", ORGLabel.class);
    }

    @Override
    public MongoDBOperatorImp<ORGLabel> getMongoDBOperation() {
        return mongoDBOperator;
    }

}