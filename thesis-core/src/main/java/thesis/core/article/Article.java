package thesis.core.article;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import org.bson.types.ObjectId;
import thesis.core.crawler.crawled_article.CrawledArticle;
import thesis.utils.dto.CommonDTO;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
public class Article extends CommonDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String title;
    private String location;
    private String description;
    private String content;
    private String url;
    private Long publicationDate;
    private List<String> authors;
    private List<String> topics;
    private List<String> labels;
    private List<CrawledArticle.Image> images;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class Image {
        private String url;
        private String description;
    }
}
