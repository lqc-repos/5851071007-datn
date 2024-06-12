package thesis.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PaginationDTO {
    private Long total;
    private Integer page;
    private Integer size;
    private Integer totalPage;
}
