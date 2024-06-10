package thesis.core.news.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import thesis.core.crawler.crawled_article.CrawledArticle;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommandPost {
    private String title;
    private String description;
    private String content;
    private List<String> labels;
    private String topic;
    private String memberId;
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
