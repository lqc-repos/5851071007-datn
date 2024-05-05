package thesis.core.nlp.service;

import thesis.core.nlp.dto.AnnotatedWord;

import java.util.List;
import java.util.Optional;

public interface NLPService {
    Optional<AnnotatedWord> annotate(String sentence) throws Exception;

    Optional<AnnotatedWord> annotateSearch(String sentence) throws Exception;

    Optional<List<String>> addDict(List<String> dicts) throws Exception;
}
