package thesis.core.label_handler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ArticleIdfInfo {
    private String articleId;
    private List<TfIdfInfo> TfIdfInfos;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class TfIdfInfo {
        private String label;
        private String ner;
        private Double tf;
        private Double idf;
        private Double tfIdf;
    }
}
