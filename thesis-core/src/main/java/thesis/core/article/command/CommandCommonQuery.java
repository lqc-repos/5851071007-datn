package thesis.core.article.command;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class CommandCommonQuery {
    private Boolean isDescId;
    private Boolean isDescCreatedDate;
    private Boolean isDescPublicationDate;
    private Integer page;
    private Integer size;
}
