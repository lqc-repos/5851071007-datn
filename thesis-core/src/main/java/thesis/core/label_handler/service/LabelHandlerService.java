package thesis.core.label_handler.service;

import java.util.Optional;

public interface LabelHandlerService {
    Optional<Boolean> migrateArticle();

    Optional<Boolean> storageFrequency();

    Optional<Boolean> calculateTfIdf() throws Exception;

    Optional<Boolean> simulateAvgTfIdf() throws Exception;
}
