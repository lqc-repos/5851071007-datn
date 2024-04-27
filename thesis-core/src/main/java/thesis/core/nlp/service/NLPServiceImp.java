package thesis.core.nlp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.nlp.dto.AnnotatedWord;
import vn.pipeline.Annotation;
import vn.pipeline.VnCoreNLP;

import java.util.List;
import java.util.Optional;

@Service
public class NLPServiceImp implements NLPService {
    @Autowired
    private VnCoreNLP vnCoreNLP;

    @Override
    public Optional<AnnotatedWord> annotate(String sentence) throws Exception {
        Annotation annotation = new Annotation(sentence);
        vnCoreNLP.annotate(annotation);
        List<AnnotatedWord.TaggedWord> taggedWords = annotation.getWords().stream()
                .map(word -> AnnotatedWord.TaggedWord.builder()
                        .word(word.getForm())
                        .ner(word.getNerLabel())
                        .pos(word.getPosTag())
                        .dep(word.getDepLabel())
                        .build())
                .toList();
        return Optional.of(AnnotatedWord.builder()
                .sentence(sentence)
                .taggedWords(taggedWords)
                .build());
    }
}
