package thesis.core.nlp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.nlp.dto.AnnotatedWord;
import vn.corenlp.wordsegmenter.Vocabulary;
import vn.pipeline.Annotation;
import vn.pipeline.Utils;
import vn.pipeline.VnCoreNLP;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @Override
    public Optional<List<String>> addDict(List<String> dicts) throws Exception {
        String vocabPath = new File(Utils.jarDir).getParent() + "/models/wordsegmenter/vi-vocab";
        File vocabFile = new File(vocabPath);
        Set<String> existingDicts = new HashSet<>();
        if (vocabFile.exists() && vocabFile.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(vocabPath))) {
                existingDicts = (Set<String>) ois.readObject();
            }
        }
        existingDicts.addAll(dicts);
        try (FileOutputStream fos = new FileOutputStream(vocabPath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(existingDicts);
            oos.flush();
        }
        Vocabulary.loadVnDict();
        return Optional.of(dicts);
    }
}
