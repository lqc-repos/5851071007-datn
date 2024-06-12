package thesis.core.news.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommandGetListUser {
    private String memberId;
    private String searchEmail;
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;
}
