package thesis.core.nlp.service;

import thesis.core.nlp.dto.AnnotatedWord;

import java.util.Optional;

public interface NLPService {
    Optional<AnnotatedWord> annotate(String sentence) throws Exception;
}
