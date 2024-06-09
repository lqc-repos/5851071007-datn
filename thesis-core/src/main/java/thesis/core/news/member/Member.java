package thesis.core.news.member;

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
public class Member extends CommonDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String fullName;
    private String avatar;
    private String roleId;
    private String authorId;
    private List<String> savedArticles;
    private List<String> viewedArticles;
    private List<String> publishedArticles;
}
