package thesis.utils.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

public class NLPConst {
    @AllArgsConstructor
    @Getter
    public enum POS {
        PROPER_NOUN("Np"),
        CLASSIFIER_NOUN("Nc"),
        UNIT_NOUN("Nu"),
        NOUN("N"),
        ABBREVIATED_NOUN("Ny"),
        BORROWED_NOUN("Nb"),
        VERB("V"),
        BORROWED_VERB("Vb"),
        ADJECTIVE("A"),
        PRONOUN("P"),
        ADVERB("R"),
        DETERMINER("L"),
        NUMERAL_QUANTITY("M"),
        PREPOSITION("E"),
        SUBORDINATING_CONJUNCTION("C"),
        COORDINATING_CONJUNCTION("Cc"),
        INTERJECTION_EXCLAMATION("I"),
        PARTICLE_AUXILIARY_MODAL_WORDS("T"),
        ABBREVIATION("Y"),
        BOUND_MORPHEME("Z"),
        UN_DEFINITION_OTHER("X"),
        PUNCTUATION_AND_SYMBOLS("CH");
        private final String value;

        public static POS fromValue(String value) {
            return Arrays.stream(values()).filter(f -> f.value.equals(value)).findFirst().orElseThrow();
        }
    }

    @AllArgsConstructor
    @Getter
    public enum NER {
        B_PERSONS("B-PER"),
        I_PERSONS("I-PER"),
        B_LOCATIONS("B_LOC"),
        I_LOCATIONS("I_LOC"),
        B_ORGANIZATIONS("B_ORG"),
        I_ORGANIZATIONS("I_ORG"),
        MISCELLANEOUS_ENTITIES("MISC"),
        O("O");
        private final String value;

        public static NER fromValue(String value) {
            return Arrays.stream(values()).filter(f -> f.value.equals(value)).findFirst().orElseThrow();
        }
    }

    @AllArgsConstructor
    @Getter
    public enum LABEL_TYPE {
        AUTHOR,
        TOPIC,
        PER,
        ORG,
        LOC,
        UND
    }
}
