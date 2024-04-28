package thesis.core.algorithm.model.total_label.repository;

import com.mongodb.client.AggregateIterable;
import org.bson.Document;
import thesis.core.algorithm.model.total_label.TotalAlgorithmLabel;
import thesis.utils.mongo.repo.MongoDBRepository;

import java.util.Map;

public interface TotalAlgorithmLabelRepository extends MongoDBRepository<TotalAlgorithmLabel> {
    AggregateIterable<Document> aggregate(Map<String, Object> queryMatch, Map<String, Object> queryGroup);
}
