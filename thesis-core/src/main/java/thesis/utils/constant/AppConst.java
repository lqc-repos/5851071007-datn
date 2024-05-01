package thesis.utils.constant;

import java.util.Arrays;
import java.util.List;

public class AppConst {
    public static class File {
        public static final String REPORT_FOLDER = "report";
        public static final List<String> TFIDF_RATE_HEADERS = Arrays.asList("article_label_id", "label", "tf-idf");
        public static final String TFIDF_RATE_FILE_NAME = "tf-idf-rate-";
    }
}
