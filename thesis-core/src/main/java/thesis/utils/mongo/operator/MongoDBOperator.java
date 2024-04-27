package thesis.utils.mongo.operator;

import org.bson.Document;

import java.util.List;

public interface MongoDBOperator<Clazz> {
    List<Clazz> find(Document query, Document sort, int skip, int limit);

    Clazz findOne(Document query, Document sort);

    List<Clazz> findAll(Document query, Document sort, Document projection);

    Long count(Document query);

    Boolean insert(Clazz object);

    Boolean insertMany(List<Clazz> objects);

    Boolean update(Document query, Document data);
}
