package thesis;

import thesis.core.nlp.pos_tagger.POSTagger;

public class ThesisNlpApplication {

    public static void main(String[] args) throws Exception {
//        POSTagger.trainModel();
        POSTagger.getTag("Đây là dữ liệu để thử");
    }
}
