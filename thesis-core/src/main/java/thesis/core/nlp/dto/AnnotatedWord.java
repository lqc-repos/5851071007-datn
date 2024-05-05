package thesis.core.nlp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import thesis.utils.constant.NLPConst;
import thesis.utils.constant.VNExpressConst;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnnotatedWord {
    private String sentence;
    private List<TaggedWord> taggedWords;
    private Long publicationTime;
    private VNExpressConst.TOPIC topic;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class TaggedWord {
        private String word;
        private String pos;
        private String ner;
        private String dep;
        private NLPConst.LABEL_TYPE labelType;
    }
}
