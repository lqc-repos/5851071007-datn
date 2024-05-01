package thesis.core.label_handler.model.total_label_frequency.repository;

import com.mongodb.client.AggregateIterable;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import thesis.core.label_handler.model.total_label_frequency.TotalLabelFrequency;
import thesis.utils.mongo.operator.MongoDBOperatorImp;
import thesis.utils.mongo.repo.MongoDBRepositoryImp;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Repository
public class TotalLabelFrequencyRepositoryImp extends MongoDBRepositoryImp<TotalLabelFrequency> implements TotalLabelFrequencyRepository {
    private final MongoDBOperatorImp<TotalLabelFrequency> mongoDBOperator;

    public TotalLabelFrequencyRepositoryImp(@Value("${thesis.mongo.url}") String databaseUrl,
                                            @Value("${thesis.mongo.database-name}") String databaseName) {
        this.mongoDBOperator = new MongoDBOperatorImp<>(databaseUrl, databaseName, "total_label_frequency", TotalLabelFrequency.class);
    }

    @Override
    public MongoDBOperatorImp<TotalLabelFrequency> getMongoDBOperation() {
        return mongoDBOperator;
    }

    @Override
    public AggregateIterable<Document> aggregate(Map<String, Object> queryMatch, Map<String, Object> queryGroup) {
        List<Bson> bson = Arrays.asList(new Document(queryMatch), new Document(queryGroup));
        return mongoDBOperator.aggregateSpecial(bson);
    }
}