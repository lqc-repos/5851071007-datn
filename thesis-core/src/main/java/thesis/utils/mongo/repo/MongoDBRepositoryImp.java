package thesis.utils.mongo.repo;

import org.bson.Document;
import thesis.utils.dto.CommonDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class MongoDBRepositoryImp<Clazz extends CommonDTO> implements MongoDBRepository<Clazz> {
    @Override
    public List<Clazz> find(Map<String, Object> query, Map<String, Object> sort, int skip, int limit) {
        Document queryDoc = new Document(query);
        Document sortDoc = new Document(sort);
        return getMongoDBOperation().find(queryDoc, sortDoc, skip, limit);
    }

    @Override
    public List<Clazz> findAll(Map<String, Object> query, Map<String, Object> sort, Map<String, Object> projection) {
        Document queryDoc = new Document(query);
        Document sortDoc = new Document(sort);
        Document projectionDoc = new Document(projection);
        return getMongoDBOperation().findAll(queryDoc, sortDoc, projectionDoc);
    }

    @Override
    public Optional<Clazz> findOne(Map<String, Object> query, Map<String, Object> sort) {
        Document queryDoc = new Document(query);
        Document sortDoc = new Document(sort);
        Clazz results = getMongoDBOperation().findOne(queryDoc, sortDoc);
        return Optional.ofNullable(results);
    }

    @Override
    public Optional<Long> count(Document query) {
        Document queryDoc = new Document(query);
        Long results = getMongoDBOperation().count(queryDoc);
        return Optional.ofNullable(results);
    }

    @Override
    public Optional<Boolean> insert(Clazz object) {
        object.setIsDeleted(false);
        object.setCreatedDate(System.currentTimeMillis() / 1000);
        Boolean results = getMongoDBOperation().insert(object);
        return Optional.ofNullable(results);
    }

    @Override
    public Optional<Boolean> insertMany(List<Clazz> objects) {
        objects.forEach(obj -> {
            obj.setIsDeleted(false);
            obj.setCreatedDate(System.currentTimeMillis() / 1000);
        });
        Boolean results = getMongoDBOperation().insertMany(objects);
        return Optional.ofNullable(results);
    }

    @Override
    public Optional<Boolean> update(Map<String, Object> query, Map<String, Object> data) {
        Document queryDoc = new Document(query);
        Document updateDoc = new Document(data);
        updateDoc.put("updatedDate", System.currentTimeMillis() / 1000);
        Boolean results = getMongoDBOperation().update(queryDoc, new Document("$set", updateDoc));
        return Optional.ofNullable(results);
    }

    @Override
    public Optional<Boolean> increase(Map<String, Object> query, Map<String, Object> data) {
        Document queryDoc = new Document(query);
        Document updateDoc = new Document(data);
        updateDoc.put("$set", new Document("updatedDate", System.currentTimeMillis() / 1000));
        Boolean results = getMongoDBOperation().update(queryDoc, updateDoc);
        return Optional.ofNullable(results);
    }
}
