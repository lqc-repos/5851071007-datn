package thesis.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommonDTO {
    private Boolean isDeleted;
    private Long createdDate;
    private Long updatedDate;
    private Long deletedDate;
}
