package thesis.core.crawler.crawled_article.command;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class CommandCrawlArticle {
    private List<String> urls;
    private String topic;
    private String category;
    private Long fromDate;
    private Long toDate;
    private Integer page;
}
