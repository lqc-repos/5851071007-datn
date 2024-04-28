package thesis.core.algorithm.model.total_label.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import thesis.core.algorithm.model.total_label.TotalAlgorithmLabel;
import thesis.utils.mongo.operator.MongoDBOperatorImp;
import thesis.utils.mongo.repo.MongoDBRepositoryImp;

@Repository
public class TotalAlgorithmLabelRepositoryImp extends MongoDBRepositoryImp<TotalAlgorithmLabel> implements TotalAlgorithmLabelRepository {
    private final MongoDBOperatorImp<TotalAlgorithmLabel> mongoDBOperator;

    public TotalAlgorithmLabelRepositoryImp(@Value("${thesis.mongo.url}") String databaseUrl,
                                            @Value("${thesis.mongo.database-name}") String databaseName) {
        this.mongoDBOperator = new MongoDBOperatorImp<>(databaseUrl, databaseName, "total_algorithm_label", TotalAlgorithmLabel.class);
    }

    @Override
    public MongoDBOperatorImp<TotalAlgorithmLabel> getMongoDBOperation() {
        return mongoDBOperator;
    }

}