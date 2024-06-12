package thesis.core.news.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import thesis.core.news.member.Member;
import thesis.utils.dto.PaginationDTO;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
@SuperBuilder
@NoArgsConstructor
public class UserResponse extends PaginationDTO {
    private String email;
    private List<Member> members;
}
