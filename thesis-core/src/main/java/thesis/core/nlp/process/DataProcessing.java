package thesis.core.nlp.process;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import thesis.core.nlp.type_tagger.TypeTagger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@Component
@Getter
public class DataProcessing {
    private final Set<String> STOP_WORDS = new HashSet<>();

    DataProcessing() {
        loadStopWords();
    }

    private void loadStopWords() {
        try (BufferedReader br = new BufferedReader(new FileReader(TypeTagger.class.getResource("/dict/stop-words.txt").getFile(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (StringUtils.isNotBlank(line))
                    STOP_WORDS.add(line.trim().replaceAll("\\s+", "_"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
