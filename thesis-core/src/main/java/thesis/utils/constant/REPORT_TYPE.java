package thesis.utils.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum REPORT_TYPE {
    LABEL("label"),
    VIEW("view"),
    REGISTRY("registry"),
    PUBLISH("publish");

    private final String value;

    public static REPORT_TYPE fromValue(String value) {
        return Arrays.stream(values()).filter(f -> f.value.equals(value)).findFirst().orElseThrow();
    }
}
