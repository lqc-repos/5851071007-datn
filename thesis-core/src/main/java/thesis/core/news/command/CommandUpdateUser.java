package thesis.core.news.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommandUpdateUser {
    private String memberId;
    private String updateMemberId;
    private Integer roleLevel;
    private Boolean isBlocked;
    private String fullName;
}
