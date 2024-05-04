package thesis.core.configuration.repository;

import thesis.core.configuration.ThesisConfiguration;
import thesis.utils.mongo.repo.MongoDBRepository;

public interface ThesisConfigurationRepository extends MongoDBRepository<ThesisConfiguration> {
}
