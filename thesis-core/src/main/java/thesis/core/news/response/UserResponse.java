package thesis.core.news.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import thesis.utils.dto.PaginationDTO;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
@SuperBuilder
@NoArgsConstructor
public class UserResponse extends PaginationDTO {
    private String keyword;
    private List<MemberResponse> members;

    @AllArgsConstructor
    @Data
    @SuperBuilder
    @NoArgsConstructor
    public static class MemberResponse {
        private String id;
        private String fullName;
        private String email;
        private Long createdDate;
        private String role;
        private Integer roleValue;
        private Boolean isActive;
    }
}
