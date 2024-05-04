package thesis.core.label_handler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ArticleWorldInfo {
    private String ner;
    private Long count;

    public void incrementCount() {
        this.count++;
    }
}
