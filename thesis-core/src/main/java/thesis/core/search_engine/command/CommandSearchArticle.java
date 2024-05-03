package thesis.core.search_engine.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class CommandSearchArticle {
    private String search;
    private Integer page;
    private Integer size;
}