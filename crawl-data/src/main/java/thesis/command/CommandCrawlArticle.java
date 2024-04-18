package thesis.command;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class CommandCrawlArticle {
    private List<String> urls;
    private List<Topic> topics;
    private Integer page;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Topic {
        private String name;
        private List<Integer> pages;
    }
}
