package thesis.utils.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DEFAULT_ROLE {
    ADMIN("6664327f2cad8f06e1b410cf", 1),
    AUTHOR("6664327f2cad8f06e1b410d0", 2),
    MEMBER("6664327f2cad8f06e1b410d1", 3);
    private final String roleId;
    private final int numberValue;
}
