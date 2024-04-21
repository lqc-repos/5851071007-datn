package thesis.POSTagger;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class POSTagger {
    public static void trainModel() throws Exception {
        List<String> tempConfigs = new ArrayList<>();
        Properties properties = new Properties();
        InputStream input = POSTagger.class.getResourceAsStream("/pos-tagger/configs.properties");
        properties.load(input);
        properties.forEach((key, value) -> {
            tempConfigs.add("-" + key);
            tempConfigs.add(value.toString());
        });
        MaxentTagger.main(tempConfigs.toArray(new String[0]));
    }

    public static void getTag(String text) {
        String modelPath = POSTagger.class.getResource("/pos-tagger/vi-model.tagger").getPath();
        MaxentTagger tagger = new MaxentTagger(modelPath);
        for (String token : text.split(" ")) {
            TaggedWord taggedWord = new TaggedWord(token);
            List<TaggedWord> taggedWords = tagger.tagSentence(Arrays.asList(taggedWord));
            System.out.println(taggedWords.get(0).word() + " - " + taggedWords.get(0).tag());
        }
    }
}
