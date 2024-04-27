package thesis.nlp.pos_tagger;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

import java.io.InputStream;
import java.util.Properties;

public class POSTagger {
    public static void trainModel() throws Exception {
        Properties properties = new Properties();
        InputStream input = POSTagger.class.getResourceAsStream("/pos/configs.properties");
        properties.load(input);
        CRFClassifier<CoreLabel> crfClassifier = new CRFClassifier<>(properties);
        crfClassifier.train();
        crfClassifier.serializeClassifier(properties.getProperty("serializeTo"));
    }

    public static void continueTrain() throws Exception {
        Properties properties = new Properties();
        InputStream input = POSTagger.class.getResourceAsStream("/pos/configs.properties");
        properties.load(input);
        CRFClassifier<CoreLabel> crfClassifier = new CRFClassifier<>(properties);
        crfClassifier.loadClassifier(properties.getProperty("serializeTo"));
        crfClassifier.train(properties.getProperty("trainFile"));
        crfClassifier.serializeClassifier(properties.getProperty("serializeTo"));
    }

    public static void getTag(String text) throws Exception {
        CRFClassifier<CoreLabel> crfClassifier = CRFClassifier.getClassifier("D:/Learning/Repository/DATN/nlp-repository/thesis-nlp/src/main/resources/pos/pos-tagger.ser.gz");
        String tagged = crfClassifier.classifyToString(text);
        System.out.println(tagged);
    }
}
