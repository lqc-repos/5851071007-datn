package thesis.core.nlp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import thesis.core.nlp.type_tagger.TypeTagger;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnnotatedType {
    private TypeTagger.TypeClassifier typeClassifier;
    private Integer left;
    private Integer right;
}
