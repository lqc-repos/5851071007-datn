package thesis.core.search_engine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import thesis.core.article.Article;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class SearchEngineResult {
    private String search;
    private List<Article> articles;
    private Long total;
    private Integer page;
    private Integer size;
    private Integer totalPage;
}
