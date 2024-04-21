package thesis;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import thesis.POSTagger.POSTagger;

@SpringBootApplication
public class ThesisNlpApplication {

    public static void main(String[] args) throws Exception {
//        SpringApplication.run(ThesisNlpApplication.class, args);
//        POSTagger.trainModel();
        POSTagger.getTag("Đây là dữ liệu để thử");
    }
}
