package thesis.core.label_handler.model.article_label_frequency;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import org.bson.types.ObjectId;
import thesis.utils.dto.CommonDTO;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
public class ArticleLabelFrequency extends CommonDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String articleId;
    private Long totalLabel;
    private List<LabelPerArticle> labels;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class LabelPerArticle {
        private String label;
        private Long count;
        private String ner;
        private Double tf;
        private Double idf;
    }
}