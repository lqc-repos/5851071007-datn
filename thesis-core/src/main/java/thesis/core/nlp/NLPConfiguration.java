package thesis.core.nlp;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import vn.pipeline.VnCoreNLP;

@Configuration
public class NLPConfiguration {
    @PostConstruct
    void init() throws Exception {
        new VnCoreNLP();
    }
}
