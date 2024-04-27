package thesis.nlp.pos_tagger.service;

import java.util.Optional;

public interface POSTaggerService {
    Optional<Boolean> trainModel();
}
