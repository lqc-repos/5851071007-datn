package thesis.core.nlp.type_tagger;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

public class TypeDict {
    @AllArgsConstructor
    @Getter
    public enum TYPE {
        B_A("B-A"),
        B_P("B-P"),
        B_TiD("B-TiD"),
        B_TiM("B-TiM"),
        B_TiY("B-TiY"),
        I_TiD("I-TiD"),
        I_TiM("I-TiM"),
        I_TiY("I-TiY"),
        E_TiN("E-TiN"),
        E_TiD1("E-TiD1"),
        E_TiD2("E-TiD2"),
        E_TiM1("E-TiM1"),
        E_TiY1("E-TiY1");
        private String value;

        public static TYPE fromValue(String value) {
            return Arrays.stream(values()).filter(f -> f.value.equals(value)).findFirst().orElseThrow();
        }
    }
}