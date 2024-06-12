package thesis.core.search_engine.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import thesis.core.article.Article;
import thesis.utils.dto.PaginationDTO;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
@SuperBuilder
@NoArgsConstructor
public class SearchEngineResult extends PaginationDTO {
    private String search;
    private String topic;
    private List<Article> articles;
}
