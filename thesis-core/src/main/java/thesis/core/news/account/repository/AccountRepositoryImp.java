package thesis.core.news.account.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import thesis.core.news.account.Account;
import thesis.utils.mongo.operator.MongoDBOperatorImp;
import thesis.utils.mongo.repo.MongoDBRepositoryImp;

@Repository
public class AccountRepositoryImp extends MongoDBRepositoryImp<Account> implements AccountRepository {
    private final MongoDBOperatorImp<Account> mongoDBOperator;

    public AccountRepositoryImp(@Value("${thesis.mongo.url}") String databaseUrl,
                                @Value("${thesis.mongo.database-name}") String databaseName) {
        this.mongoDBOperator = new MongoDBOperatorImp<>(databaseUrl, databaseName, "account", Account.class);
    }

    @Override
    public MongoDBOperatorImp<Account> getMongoDBOperation() {
        return mongoDBOperator;
    }

}