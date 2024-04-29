package thesis.core.algorithm.model.article_label.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommandQueryArticleLabel {
    private Boolean isDescCreatedDate;
    private Integer page;
    private Integer size;
}
