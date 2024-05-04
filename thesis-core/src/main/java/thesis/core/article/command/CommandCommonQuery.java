package thesis.core.article.command;

import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class CommandCommonQuery {
    private Set<String> articleIds;
    private Boolean isDescId;
    private Boolean isDescCreatedDate;
    private Boolean isDescPublicationDate;
    private Integer page;
    private Integer size;
}
