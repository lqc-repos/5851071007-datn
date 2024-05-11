package thesis.utils.mongo.operator;

import com.mongodb.client.AggregateIterable;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;

public interface MongoDBOperator<Clazz> {
    List<Clazz> find(Document query, Document sort, int skip, int limit);

    List<Clazz> find(Document query, Document sort, Document projection, int skip, int limit);

    Clazz findOne(Document query, Document sort);

    List<Clazz> find(Document query, Document sort, Document projection);

    Long count(Document query);

    Boolean insert(Clazz object);

    Boolean insertMany(List<Clazz> objects);

    Boolean update(Document query, Document data);

    AggregateIterable<Document> aggregateSpecial(List<Bson> pipeline);
}
