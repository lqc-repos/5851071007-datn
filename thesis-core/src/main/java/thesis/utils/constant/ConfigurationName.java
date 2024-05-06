package thesis.utils.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ConfigurationName {
    ELIGIBLE_RATE("eligible_rate"),
    LABEL_SCORE("label_score"),
    STOP_WORD("stop_word");
    private final String name;
}
