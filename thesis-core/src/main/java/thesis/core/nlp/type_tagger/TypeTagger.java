package thesis.core.nlp.type_tagger;

import lombok.*;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class TypeTagger {
    private final List<TypeClassifier> TYPE_CLASSIFIERS = new ArrayList<>();

    TypeTagger() {
        try (BufferedReader br = new BufferedReader(new FileReader(TypeTagger.class.getResource("/dict/type-tagger-dict.txt").getFile(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split(" ");
                if (words.length != 2) throw new Exception("Training data is incorrect format");
                TYPE_CLASSIFIERS.add(TypeClassifier.builder()
                        .word(words[0])
                        .type(words[1])
                        .build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class TypeClassifier {
        private String word;
        private String type;
    }
}
