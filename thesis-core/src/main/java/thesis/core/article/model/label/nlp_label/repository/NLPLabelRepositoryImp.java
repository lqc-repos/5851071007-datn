package thesis.core.article.model.label.nlp_label.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import thesis.core.article.model.label.nlp_label.NLPLabel;
import thesis.utils.mongo.operator.MongoDBOperatorImp;
import thesis.utils.mongo.repo.MongoDBRepositoryImp;

@Repository
public class NLPLabelRepositoryImp extends MongoDBRepositoryImp<NLPLabel> implements NLPLabelRepository {
    private final MongoDBOperatorImp<NLPLabel> mongoDBOperator;

    public NLPLabelRepositoryImp(@Value("${thesis.mongo.url}") String databaseUrl,
                                 @Value("${thesis.mongo.database-name}") String databaseName) {
        this.mongoDBOperator = new MongoDBOperatorImp<>(databaseUrl, databaseName, "nlp_label", NLPLabel.class);
    }

    @Override
    public MongoDBOperatorImp<NLPLabel> getMongoDBOperation() {
        return mongoDBOperator;
    }

}