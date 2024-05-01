package thesis.core.label_handler.model.article_label_frequency.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommandQueryArticleLabelFrequency {
    private Boolean isDescCreatedDate;
    private Integer page;
    private Integer size;
}
