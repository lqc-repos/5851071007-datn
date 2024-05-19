package thesis.news.search.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommandSearch {
    private String search;
    private String topic;
    private Integer page;
    private Integer size;
    private Boolean isCustomTag;
}
