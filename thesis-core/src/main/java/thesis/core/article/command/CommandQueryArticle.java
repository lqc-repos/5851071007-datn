package thesis.core.article.command;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class CommandQueryArticle {
    private Boolean isDescCreatedDate;
    private Boolean isDescPublicationDate;
    private Integer page;
    private Integer size;
}
