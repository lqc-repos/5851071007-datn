package thesis.news.article.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Article {
    private String id;
    private String title;
    private String location;
    private String description;
    private String content;
    private String url;
    private Long publicationDate;
    private List<String> authors;
    private List<String> topics;
    private List<String> labels;
}
