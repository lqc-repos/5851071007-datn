package thesis.core.nlp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnnotatedWord {
    private String sentence;
    private List<TaggedWord> taggedWords;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class TaggedWord {
        private String word;
        private String pos;
        private String ner;
        private String dep;
    }
}
