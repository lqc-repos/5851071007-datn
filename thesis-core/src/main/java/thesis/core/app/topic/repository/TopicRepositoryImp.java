package thesis.core.app.topic.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import thesis.core.app.topic.Topic;
import thesis.utils.mongo.operator.MongoDBOperatorImp;
import thesis.utils.mongo.repo.MongoDBRepositoryImp;

@Repository
public class TopicRepositoryImp extends MongoDBRepositoryImp<Topic> implements TopicRepository {
    private final MongoDBOperatorImp<Topic> mongoDBOperator;

    public TopicRepositoryImp(@Value("${thesis.mongo.url}") String databaseUrl,
                              @Value("${thesis.mongo.database-name}") String databaseName) {
        this.mongoDBOperator = new MongoDBOperatorImp<>(databaseUrl, databaseName, "topic", Topic.class);
    }

    @Override
    public MongoDBOperatorImp<Topic> getMongoDBOperation() {
        return mongoDBOperator;
    }

}