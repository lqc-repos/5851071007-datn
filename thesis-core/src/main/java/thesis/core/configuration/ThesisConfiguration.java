package thesis.core.configuration;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import org.bson.types.ObjectId;
import thesis.utils.dto.CommonDTO;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
public class ThesisConfiguration extends CommonDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String name;
    private String value;
}
