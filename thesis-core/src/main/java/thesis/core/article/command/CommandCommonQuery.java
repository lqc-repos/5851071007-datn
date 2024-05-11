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
    private Set<String> topics;
    private Set<String> projections;
    private Boolean isDescId;
    private Boolean isDescCreatedDate;
    private Boolean isDescPublicationDate;
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;
}
