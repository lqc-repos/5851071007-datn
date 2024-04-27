package thesis.core.nlp;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.pipeline.VnCoreNLP;

@Configuration
@ConditionalOnProperty(
        prefix = "nlp",
        name = "is-active",
        havingValue = "true"
)
public class NLPConfiguration {
    private VnCoreNLP vnCoreNLP;

    @PostConstruct
    void init() throws Exception {
        if (vnCoreNLP == null)
            vnCoreNLP = new VnCoreNLP();
    }

    @Bean("vnCoreNLP")
    public VnCoreNLP vnCoreNLP() {
        return vnCoreNLP;
    }
}
