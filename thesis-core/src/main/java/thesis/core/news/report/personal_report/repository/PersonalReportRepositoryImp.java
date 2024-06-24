package thesis.core.news.report.personal_report.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import thesis.core.news.report.personal_report.PersonalReport;
import thesis.utils.mongo.operator.MongoDBOperatorImp;
import thesis.utils.mongo.repo.MongoDBRepositoryImp;

@Repository
public class PersonalReportRepositoryImp extends MongoDBRepositoryImp<PersonalReport> implements PersonalReportRepository {
    private final MongoDBOperatorImp<PersonalReport> mongoDBOperator;

    public PersonalReportRepositoryImp(@Value("${thesis.mongo.url}") String databaseUrl,
                                       @Value("${thesis.mongo.database-name}") String databaseName) {
        this.mongoDBOperator = new MongoDBOperatorImp<>(databaseUrl, databaseName, "personal_report", PersonalReport.class);
    }

    @Override
    public MongoDBOperatorImp<PersonalReport> getMongoDBOperation() {
        return mongoDBOperator;
    }

}