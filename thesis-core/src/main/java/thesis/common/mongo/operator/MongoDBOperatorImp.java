package thesis.common.mongo.operator;

import com.mongodb.client.MongoCollection;
import org.apache.commons.collections4.MapUtils;
import org.bson.BsonValue;
import org.bson.Document;
import thesis.common.mongo.provider.MongoDBProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MongoDBOperatorImp<Clazz> implements MongoDBOperator<Clazz> {
    private final MongoCollection<Clazz> mongoCollection;

    public MongoDBOperatorImp(String connectionString, String databaseName, String collectionName, Class<Clazz> clazz) {
        this.mongoCollection = MongoDBProvider.getMongoDBInstance(connectionString, databaseName)
                .getCollection(collectionName, clazz);
    }

    @Override
    public List<Clazz> find(Document query, Document sort, int skip, int limit) {
        return this.mongoCollection.find(query).sort(sort).skip(skip).limit(limit).into(new ArrayList<>());
    }

    @Override
    public Clazz findOne(Document query, Document sort) {
        return this.mongoCollection.find(query).sort(sort).first();
    }

    @Override
    public List<Clazz> findAll(Document query, Document sort, Document projection) {
        return this.mongoCollection.find(query).sort(sort).projection(projection).into(new ArrayList<>());
    }

    @Override
    public Long count(Document query) {
        return this.mongoCollection.countDocuments(query);
    }

    @Override
    public Boolean insert(Clazz object) {
        BsonValue generatedId = this.mongoCollection.insertOne(object).getInsertedId();
        return Objects.nonNull(generatedId);
    }

    @Override
    public Boolean insertMany(List<Clazz> objects) {
        Map<Integer, BsonValue> generatedIds = this.mongoCollection.insertMany(objects).getInsertedIds();
        if (MapUtils.isEmpty(generatedIds))
            return false;
        return generatedIds.size() == objects.size();
    }

    @Override
    public Boolean update(Document query, Document data) {
        return this.mongoCollection.updateMany(query, data).getMatchedCount() > 0;
    }
}
