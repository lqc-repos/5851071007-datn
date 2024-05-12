package thesis.core.nlp.service;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.nlp.dto.AnnotatedType;
import thesis.core.nlp.dto.AnnotatedWord;
import thesis.core.nlp.type_tagger.TypeDict;
import thesis.core.nlp.type_tagger.TypeTagger;
import thesis.utils.constant.NLPConst;
import thesis.utils.constant.VNExpressConst;
import vn.corenlp.wordsegmenter.Vocabulary;
import vn.pipeline.Annotation;
import vn.pipeline.Utils;
import vn.pipeline.VnCoreNLP;

import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class NLPServiceImp implements NLPService {
    public static final int MAX_LEFT = 5, MAX_RIGHT = 5;
    private static final ZoneId ZONE_ID = ZoneId.of("GMT+7");
    @Autowired
    private VnCoreNLP vnCoreNLP;
    @Autowired
    private TypeTagger typeTagger;

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
    public Optional<AnnotatedWord> annotateSearch(String sentence) throws Exception {
        if (StringUtils.isBlank(sentence))
            return Optional.empty();
        Optional<AnnotatedWord> annotatedWordOptional = annotate(sentence);
        if (annotatedWordOptional.isEmpty())
            return Optional.empty();

        List<TypeTagger.TypeClassifier> typeClassifiers = typeTagger.getTYPE_CLASSIFIERS();

        List<AnnotatedType> annotatedTypes = new ArrayList<>();
        AnnotatedWord annotatedWord = annotatedWordOptional.get();

        for (AnnotatedWord.TaggedWord taggedWord : annotatedWord.getTaggedWords()) {
            switch (NLPConst.NER.fromValue(taggedWord.getNer())) {
                case B_PERSONS, I_PERSONS -> taggedWord.setLabelType(NLPConst.LABEL_TYPE.PER);
                case B_LOCATIONS, I_LOCATIONS -> taggedWord.setLabelType(NLPConst.LABEL_TYPE.LOC);
                case B_ORGANIZATIONS, I_ORGANIZATIONS -> taggedWord.setLabelType(NLPConst.LABEL_TYPE.ORG);
                default -> {
                    if (VNExpressConst.TOPIC.getVnValues().contains(taggedWord.getWord().toLowerCase())) {
                        taggedWord.setLabelType(NLPConst.LABEL_TYPE.TOPIC);
                    } else {
                        taggedWord.setLabelType(NLPConst.LABEL_TYPE.UND);
                    }
                }
            }
        }

        for (int left = 0; left < annotatedWord.getTaggedWords().size() - 1; left++) {
            StringBuilder stringBuilder = new StringBuilder();
            int maxRight = Math.min(MAX_RIGHT + left, annotatedWord.getTaggedWords().size());
            for (int right = left; right < maxRight; right++) {
                stringBuilder.append(annotatedWord.getTaggedWords().get(right).getWord());
                List<TypeTagger.TypeClassifier> containClassifiers = typeClassifiers.stream()
                        .filter(typeClassifier -> typeClassifier.getWord().equals(stringBuilder.toString().trim().toLowerCase()))
                        .toList();
                if (CollectionUtils.isNotEmpty(containClassifiers)) {
                    for (TypeTagger.TypeClassifier typeClassifier : containClassifiers) {
                        annotatedTypes.add(AnnotatedType.builder()
                                .typeClassifier(typeClassifier)
                                .left(left)
                                .right(right)
                                .build());
                    }
                }
                stringBuilder.append("_");
            }
        }

        annotatedWord.setPublicationTime(LocalDateTime.now().atZone(ZONE_ID).toEpochSecond());
        for (AnnotatedType annotatedType : annotatedTypes) {
            switch (TypeDict.TYPE.fromValue(annotatedType.getTypeClassifier().getType())) {
                case B_A -> {
                    int maxRight = Math.min(MAX_RIGHT + annotatedType.getLeft(), annotatedWord.getTaggedWords().size());
                    for (int right = annotatedType.getRight() + 1; right < maxRight; right++) {
                        AnnotatedWord.TaggedWord taggedW = annotatedWord.getTaggedWords().get(right);
                        if (taggedW.getPos().equals(NLPConst.POS.PROPER_NOUN.getValue())
                                && Arrays.asList(NLPConst.NER.B_PERSONS.getValue(),
                                NLPConst.NER.I_PERSONS.getValue()).contains(taggedW.getNer())) {
                            taggedW.setLabelType(NLPConst.LABEL_TYPE.AUTHOR);
                        }
                    }
                }
                case E_TiN -> annotatedWord.setPublicationTime(LocalDateTime.now().atZone(ZONE_ID).toEpochSecond());
                case E_TiD1 -> annotatedWord.setPublicationTime(LocalDateTime.now()
                        .minusDays(1L).atZone(ZONE_ID).toEpochSecond());
                case E_TiD2 -> annotatedWord.setPublicationTime(LocalDateTime.now()
                        .minusDays(2L).atZone(ZONE_ID).toEpochSecond());
                case E_TiM1 -> annotatedWord.setPublicationTime(LocalDateTime.now()
                        .minusMonths(1L).atZone(ZONE_ID).toEpochSecond());
                case E_TiY1 -> annotatedWord.setPublicationTime(LocalDateTime.now()
                        .minusYears(1L).atZone(ZONE_ID).toEpochSecond());
                case B_TiD -> {
                    AnnotatedWord.TaggedWord taggedW = annotatedWord.getTaggedWords()
                            .get(Math.min(annotatedType.getRight() + 1, annotatedWord.getTaggedWords().size()));
                    if (taggedW.getPos().equals(NLPConst.POS.NUMERAL_QUANTITY.getValue())) {
                        annotatedWord.setPublicationTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(annotatedWord.getPublicationTime()), ZONE_ID)
                                .withDayOfMonth(Integer.parseInt(taggedW.getWord())).atZone(ZONE_ID).toEpochSecond());
                    }
                }
                case B_TiM -> {
                    AnnotatedWord.TaggedWord taggedW = annotatedWord.getTaggedWords()
                            .get(Math.min(annotatedType.getRight() + 1, annotatedWord.getTaggedWords().size()));
                    if (taggedW.getPos().equals(NLPConst.POS.NUMERAL_QUANTITY.getValue())) {
                        annotatedWord.setPublicationTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(annotatedWord.getPublicationTime()), ZONE_ID)
                                .withMonth(Integer.parseInt(taggedW.getWord())).atZone(ZONE_ID).toEpochSecond());
                    }
                }
                case B_TiY -> {
                    AnnotatedWord.TaggedWord taggedW = annotatedWord.getTaggedWords()
                            .get(Math.min(annotatedType.getRight() + 1, annotatedWord.getTaggedWords().size()));
                    if (taggedW.getPos().equals(NLPConst.POS.NUMERAL_QUANTITY.getValue())) {
                        annotatedWord.setPublicationTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(annotatedWord.getPublicationTime()), ZONE_ID)
                                .withYear(Integer.parseInt(taggedW.getWord())).atZone(ZONE_ID).toEpochSecond());
                    }
                }
                case I_TiD -> {
                    AnnotatedWord.TaggedWord taggedW = annotatedWord.getTaggedWords()
                            .get(Math.max(annotatedType.getLeft() - 1, 0));
                    if (taggedW.getPos().equals(NLPConst.POS.NUMERAL_QUANTITY.getValue())) {
                        annotatedWord.setPublicationTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(annotatedWord.getPublicationTime()), ZONE_ID)
                                .minusDays(Integer.parseInt(taggedW.getWord())).atZone(ZONE_ID).toEpochSecond());
                    }
                }
                case I_TiM -> {
                    AnnotatedWord.TaggedWord taggedW = annotatedWord.getTaggedWords()
                            .get(Math.max(annotatedType.getLeft() - 1, 0));
                    if (taggedW.getPos().equals(NLPConst.POS.NUMERAL_QUANTITY.getValue())) {
                        annotatedWord.setPublicationTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(annotatedWord.getPublicationTime()), ZONE_ID)
                                .minusMonths(Integer.parseInt(taggedW.getWord())).atZone(ZONE_ID).toEpochSecond());
                    }
                }
                case I_TiY -> {
                    AnnotatedWord.TaggedWord taggedW = annotatedWord.getTaggedWords()
                            .get(Math.max(annotatedType.getLeft() - 1, 0));
                    if (taggedW.getPos().equals(NLPConst.POS.NUMERAL_QUANTITY.getValue())) {
                        annotatedWord.setPublicationTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(annotatedWord.getPublicationTime()), ZONE_ID)
                                .minusYears(Integer.parseInt(taggedW.getWord())).atZone(ZONE_ID).toEpochSecond());
                    }
                }
            }
        }

        return Optional.of(annotatedWord);
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
