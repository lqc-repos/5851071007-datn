package thesis.news.article.dto;

import lombok.*;
import thesis.utils.dto.CommonForm;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class ArticleForm extends CommonForm {
    private List<Article> articles;
    private String topic;
}
