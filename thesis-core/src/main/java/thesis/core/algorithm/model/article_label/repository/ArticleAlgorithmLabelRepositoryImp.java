package thesis.core.algorithm.model.article_label.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import thesis.core.algorithm.model.article_label.ArticleAlgorithmLabel;
import thesis.utils.mongo.operator.MongoDBOperatorImp;
import thesis.utils.mongo.repo.MongoDBRepositoryImp;

@Repository
public class ArticleAlgorithmLabelRepositoryImp extends MongoDBRepositoryImp<ArticleAlgorithmLabel> implements ArticleAlgorithmLabelRepository {
    private final MongoDBOperatorImp<ArticleAlgorithmLabel> mongoDBOperator;

    public ArticleAlgorithmLabelRepositoryImp(@Value("${thesis.mongo.url}") String databaseUrl,
                                              @Value("${thesis.mongo.database-name}") String databaseName) {
        this.mongoDBOperator = new MongoDBOperatorImp<>(databaseUrl, databaseName, "article_algorithm_label", ArticleAlgorithmLabel.class);
    }

    @Override
    public MongoDBOperatorImp<ArticleAlgorithmLabel> getMongoDBOperation() {
        return mongoDBOperator;
    }

}