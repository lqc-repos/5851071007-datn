package thesis.core.news.member.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import thesis.core.news.member.Member;
import thesis.utils.mongo.operator.MongoDBOperatorImp;
import thesis.utils.mongo.repo.MongoDBRepositoryImp;

@Repository
public class MemberRepositoryImp extends MongoDBRepositoryImp<Member> implements MemberRepository {
    private final MongoDBOperatorImp<Member> mongoDBOperator;

    public MemberRepositoryImp(@Value("${thesis.mongo.url}") String databaseUrl,
                               @Value("${thesis.mongo.database-name}") String databaseName) {
        this.mongoDBOperator = new MongoDBOperatorImp<>(databaseUrl, databaseName, "member", Member.class);
    }

    @Override
    public MongoDBOperatorImp<Member> getMongoDBOperation() {
        return mongoDBOperator;
    }

}