package thesis.utils.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DEFAULT_ROLE {
    ADMIN("6664327f2cad8f06e1b410cf", "Admin", 1),
    AUTHOR("6664327f2cad8f06e1b410d0", "Author", 2),
    MEMBER("6664327f2cad8f06e1b410d1", "Member", 3);
    private final String roleId;
    private final String role;
    private final int numberValue;

    public static String getRoleIdByNumberValue(int numberValue) {
        for (DEFAULT_ROLE role : DEFAULT_ROLE.values()) {
            if (role.getNumberValue() == numberValue) {
                return role.getRoleId();
            }
        }
        return null;
    }

    public static String getRoleById(String roleId) {
        for (DEFAULT_ROLE role : DEFAULT_ROLE.values()) {
            if (role.getRoleId().equals(roleId)) {
                return role.getRole();
            }
        }
        return null;
    }

    public static Integer getRoleNum(String roleId) {
        for (DEFAULT_ROLE role : DEFAULT_ROLE.values()) {
            if (role.getRoleId().equals(roleId)) {
                return role.getNumberValue();
            }
        }
        return null;
    }
}
