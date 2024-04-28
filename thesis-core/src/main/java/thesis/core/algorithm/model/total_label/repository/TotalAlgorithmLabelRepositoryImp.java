package thesis.core.algorithm.model.total_label.repository;

import com.mongodb.client.AggregateIterable;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import thesis.core.algorithm.model.total_label.TotalAlgorithmLabel;
import thesis.utils.mongo.operator.MongoDBOperatorImp;
import thesis.utils.mongo.repo.MongoDBRepositoryImp;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    @Override
    public AggregateIterable<Document> aggregate(Map<String, Object> queryMatch, Map<String, Object> queryGroup) {
//        BasicDBObject match = BasicDBObject.parse(JSONUtils.objectToJson().toString());
//        BasicDBObject group = BasicDBObject.parse(JSONUtils.objectToJson(queryGroup).toString());
        List<Bson> bson = Arrays.asList(new Document(queryMatch), new Document(queryGroup));
        return mongoDBOperator.aggregateSpecial(bson);
    }
}