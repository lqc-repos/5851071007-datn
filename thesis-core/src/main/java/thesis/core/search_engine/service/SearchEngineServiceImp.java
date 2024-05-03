package thesis.core.search_engine.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.article.model.author.service.AuthorService;
import thesis.core.article.model.label.custom_label.service.CustomLabelService;
import thesis.core.article.model.label.nlp_label.service.NLPLabelService;
import thesis.core.article.model.label.org_label.service.ORGLabelService;
import thesis.core.article.model.label.per_label.service.PERLabelService;
import thesis.core.article.model.location.service.LocationService;
import thesis.core.article.model.topic.service.TopicService;
import thesis.core.article.service.ArticleService;
import thesis.core.nlp.dto.AnnotatedWord;
import thesis.core.nlp.service.NLPService;
import thesis.core.search_engine.command.CommandSearchArticle;
import thesis.core.search_engine.dto.SearchEngineResult;

import java.util.Optional;

@Service
public class SearchEngineServiceImp implements SearchEngineService {
    @Autowired
    private NLPService nlpService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private CustomLabelService customLabelService;
    @Autowired
    private NLPLabelService nlpLabelService;
    @Autowired
    private ORGLabelService orgLabelService;
    @Autowired
    private PERLabelService perLabelService;

    @Override
    public Optional<SearchEngineResult> searchArticle(CommandSearchArticle command) throws Exception {
        if (StringUtils.isBlank(command.getSearch()))
            return Optional.empty();
        Optional<AnnotatedWord> annotatedWordOptional = nlpService.annotate(command.getSearch());
        if (annotatedWordOptional.isEmpty())
            throw new Exception(String.format("Can not annotate the search text \"%s\"", command.getSearch()));

        return Optional.empty();
    }
}
