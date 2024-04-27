package thesis.core.utils.mongo.provider;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MongoDBProvider {
    public static final Map<String, MongoDatabase> mongoDBProviders = new ConcurrentHashMap<>();

    public static MongoDatabase getMongoDBInstance(String connectionURL, String databaseName) {
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        if (mongoDBProviders.containsKey(databaseName))
            return mongoDBProviders.get(databaseName).withCodecRegistry(codecRegistry);
        MongoClient mongoClient = MongoClients.create(connectionURL);
        mongoDBProviders.put(databaseName, mongoClient.getDatabase(databaseName));
        return mongoDBProviders.get(databaseName).withCodecRegistry(codecRegistry);
    }
}
