package thesis.core.news.role;

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
public class Role extends CommonDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String role;
    private Integer numberValue;
    private List<String> accessPath;
}
