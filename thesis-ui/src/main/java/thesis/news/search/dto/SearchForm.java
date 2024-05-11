package thesis.news.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import thesis.news.article.dto.Article;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SearchForm {
    private String search;
    private List<Article> articles;
    private Long total;
    private Integer page;
    private Integer size;
    private Integer totalPage;
}
