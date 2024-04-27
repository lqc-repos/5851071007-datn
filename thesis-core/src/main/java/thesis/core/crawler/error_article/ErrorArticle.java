package thesis.core.crawler.error_article;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import org.bson.types.ObjectId;
import thesis.common.dto.CommonDTO;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
public class ErrorArticle extends CommonDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String url;
    private String reason;
}
