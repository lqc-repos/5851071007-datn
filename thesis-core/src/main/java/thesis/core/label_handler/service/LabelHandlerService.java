package thesis.core.label_handler.service;

import java.util.Optional;

public interface LabelHandlerService {
    Optional<Boolean> migrateArticle();

    Optional<Boolean> storeFrequency();

    Optional<Boolean> divideArticleComponent();

    Optional<Boolean> calculateTf() throws Exception;

    Optional<Boolean> handleNLPLabel() throws Exception;
}
