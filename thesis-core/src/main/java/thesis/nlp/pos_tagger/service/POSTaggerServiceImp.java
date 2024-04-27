package thesis.nlp.pos_tagger.service;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;

@Service
public class POSTaggerServiceImp implements POSTaggerService {
    @Value("${nlp.pos.config-file-path}")
    private String configFilePath;
    @Value("${nlp.pos.folder-path}")
    private String posFolderPath;
    @Value("${nlp.pos.data-training-file-name}")
    private String dataTrainingFileName;
    @Value("${nlp.pos.serialize-to-file-name}")
    private String serializeToFileName;

    private static CRFClassifier<CoreLabel> posClassifier = null;

    @PostConstruct
    void init() throws Exception {
        try {
            posClassifier = loadModel();
        } catch (Exception ex) {
            throw new Exception("Can not init POS model");
        }
    }

    @Override
    public Optional<String> annotate(String textToTag) {
        return Optional.of(posClassifier.classifyToString(textToTag));
    }

    public CRFClassifier<CoreLabel> loadModel() throws Exception {
        Properties properties = new Properties();
        properties.load(POSTaggerServiceImp.class.getResourceAsStream(configFilePath));
        CRFClassifier<CoreLabel> classifier = new CRFClassifier<>(properties);
        String folderPath = POSTaggerServiceImp.class.getResource(posFolderPath).getPath();
        if (folderPath.startsWith("/"))
            folderPath = folderPath.substring(1);
        String serializeToFile = String.format("%s%s%s", folderPath, File.separator, serializeToFileName);
        String trainingDataFile = String.format("%s%s%s", folderPath, File.separator, dataTrainingFileName);
        if (!Files.exists(Path.of(serializeToFile))) {
            classifier.train(trainingDataFile);
            classifier.serializeClassifier(serializeToFile);
            return classifier;
        }
        classifier.loadClassifier(serializeToFile);
        return classifier;
    }
}
