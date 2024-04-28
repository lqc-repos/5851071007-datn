package thesis.core.algorithm.service;

import java.util.Optional;

public interface AlgorithmService {
    Optional<Boolean> migrateArticle();

    Optional<Boolean> saveFrequency() throws Exception;
}
