package thesis.core.news.report.news_report.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import thesis.core.news.report.news_report.NewsReport;
import thesis.utils.mongo.operator.MongoDBOperatorImp;
import thesis.utils.mongo.repo.MongoDBRepositoryImp;

@Repository
public class NewsReportRepositoryImp extends MongoDBRepositoryImp<NewsReport> implements NewsReportRepository {
    private final MongoDBOperatorImp<NewsReport> mongoDBOperator;

    public NewsReportRepositoryImp(@Value("${thesis.mongo.url}") String databaseUrl,
                                   @Value("${thesis.mongo.database-name}") String databaseName) {
        this.mongoDBOperator = new MongoDBOperatorImp<>(databaseUrl, databaseName, "news_report", NewsReport.class);
    }

    @Override
    public MongoDBOperatorImp<NewsReport> getMongoDBOperation() {
        return mongoDBOperator;
    }

}