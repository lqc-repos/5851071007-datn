package thesis.core.news.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommandGetArticleByMember {
    private List<String> articleIds;
    private String memberId;
    private String type;
    private Integer page;
    private Integer size;
}
