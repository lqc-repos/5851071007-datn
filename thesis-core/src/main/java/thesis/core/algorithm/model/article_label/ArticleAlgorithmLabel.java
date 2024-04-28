package thesis.core.algorithm.model.article_label;

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
public class ArticleAlgorithmLabel extends CommonDTO {
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
    }
}