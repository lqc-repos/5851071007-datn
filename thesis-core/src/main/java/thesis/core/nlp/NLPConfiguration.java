package thesis.core.nlp;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import vn.pipeline.VnCoreNLP;

@Configuration
@ConditionalOnProperty(
        prefix = "nlp",
        name = "is-active",
        havingValue = "true"
)
public class NLPConfiguration {
    @PostConstruct
    void init() throws Exception {
        new VnCoreNLP();
    }
}
