package thesis.utils.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommonForm {
    private Long total;
    private Integer page;
    private Integer size;
    private Integer totalPage;
}
