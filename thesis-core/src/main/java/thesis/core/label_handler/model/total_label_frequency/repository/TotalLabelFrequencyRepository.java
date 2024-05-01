package thesis.core.label_handler.model.total_label_frequency.repository;

import com.mongodb.client.AggregateIterable;
import org.bson.Document;
import thesis.core.label_handler.model.total_label_frequency.TotalLabelFrequency;
import thesis.utils.mongo.repo.MongoDBRepository;

import java.util.Map;

public interface TotalLabelFrequencyRepository extends MongoDBRepository<TotalLabelFrequency> {
    AggregateIterable<Document> aggregate(Map<String, Object> queryMatch, Map<String, Object> queryGroup);
}
