package thesis.core.configuration.repository;

import thesis.core.configuration.ThesisConfiguration;
import thesis.utils.mongo.repo.MongoDBRepository;

public interface AuthorRepository extends MongoDBRepository<ThesisConfiguration> {
}
