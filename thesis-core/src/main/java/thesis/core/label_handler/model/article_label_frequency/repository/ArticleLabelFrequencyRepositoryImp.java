package thesis.core.label_handler.model.article_label_frequency.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import thesis.core.label_handler.model.article_label_frequency.ArticleLabelFrequency;
import thesis.utils.mongo.operator.MongoDBOperatorImp;
import thesis.utils.mongo.repo.MongoDBRepositoryImp;

@Repository
public class ArticleLabelFrequencyRepositoryImp extends MongoDBRepositoryImp<ArticleLabelFrequency> implements ArticleLabelFrequencyRepository {
    private final MongoDBOperatorImp<ArticleLabelFrequency> mongoDBOperator;

    public ArticleLabelFrequencyRepositoryImp(@Value("${thesis.mongo.url}") String databaseUrl,
                                              @Value("${thesis.mongo.database-name}") String databaseName) {
        this.mongoDBOperator = new MongoDBOperatorImp<>(databaseUrl, databaseName, "article_label_frequency", ArticleLabelFrequency.class);
    }

    @Override
    public MongoDBOperatorImp<ArticleLabelFrequency> getMongoDBOperation() {
        return mongoDBOperator;
    }

}