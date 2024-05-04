package thesis.core.search_engine;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import thesis.core.article.command.CommandCommonQuery;
import thesis.core.article.model.author.Author;
import thesis.core.article.model.author.service.AuthorService;
import thesis.core.article.model.label.custom_label.CustomLabel;
import thesis.core.article.model.label.custom_label.service.CustomLabelService;
import thesis.core.article.model.label.loc_label.LOCLabel;
import thesis.core.article.model.label.loc_label.service.LOCLabelService;
import thesis.core.article.model.label.nlp_label.NLPLabel;
import thesis.core.article.model.label.nlp_label.service.NLPLabelService;
import thesis.core.article.model.label.org_label.ORGLabel;
import thesis.core.article.model.label.org_label.service.ORGLabelService;
import thesis.core.article.model.label.per_label.PERLabel;
import thesis.core.article.model.label.per_label.service.PERLabelService;
import thesis.core.article.model.location.Location;
import thesis.core.article.model.location.service.LocationService;
import thesis.core.article.model.topic.Topic;
import thesis.core.article.model.topic.service.TopicService;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@Getter
@Log4j2
public class SearchEngine {
    private final Map<String, Author> authorMap;
    private final Map<String, Topic> topicMap;
    private final Map<String, Location> locationMap;
    private final Map<String, CustomLabel> customLabelMap;
    private final Map<String, NLPLabel> nlpLabelMap;
    private final Map<String, PERLabel> perLabelMap;
    private final Map<String, ORGLabel> orgLabelMap;
    private final Map<String, LOCLabel> locLabelMap;

    @Autowired
    SearchEngine(AuthorService authorService,
                 TopicService topicService,
                 LocationService locationService,
                 CustomLabelService customLabelService,
                 NLPLabelService nlpLabelService,
                 ORGLabelService orgLabelService,
                 PERLabelService perLabelService,
                 LOCLabelService locLabelService) {
        log.info("=== start init label maps");
        authorMap = authorService.getMany(CommandCommonQuery.builder()
                .page(1)
                .size(Integer.MAX_VALUE)
                .build()).stream().collect(Collectors.toMap(Author::getAuthor, author -> author));
        topicMap = topicService.getMany(CommandCommonQuery.builder()
                .page(1)
                .size(Integer.MAX_VALUE)
                .build()).stream().collect(Collectors.toMap(Topic::getTopic, topic -> topic));
        locationMap = locationService.getMany(CommandCommonQuery.builder()
                .page(1)
                .size(Integer.MAX_VALUE)
                .build()).stream().collect(Collectors.toMap(Location::getLocation, location -> location));
        customLabelMap = customLabelService.getMany(CommandCommonQuery.builder()
                .page(1)
                .size(Integer.MAX_VALUE)
                .build()).stream().collect(Collectors.toMap(CustomLabel::getLabel, customLabel -> customLabel));
        nlpLabelMap = nlpLabelService.getMany(CommandCommonQuery.builder()
                .page(1)
                .size(Integer.MAX_VALUE)
                .build()).stream().collect(Collectors.toMap(NLPLabel::getLabel, nlpLabel -> nlpLabel));
        perLabelMap = perLabelService.getMany(CommandCommonQuery.builder()
                .page(1)
                .size(Integer.MAX_VALUE)
                .build()).stream().collect(Collectors.toMap(PERLabel::getLabel, perLabel -> perLabel));
        orgLabelMap = orgLabelService.getMany(CommandCommonQuery.builder()
                .page(1)
                .size(Integer.MAX_VALUE)
                .build()).stream().collect(Collectors.toMap(ORGLabel::getLabel, orgLabel -> orgLabel));
        locLabelMap = locLabelService.getMany(CommandCommonQuery.builder()
                .page(1)
                .size(Integer.MAX_VALUE)
                .build()).stream().collect(Collectors.toMap(LOCLabel::getLabel, locLabel -> locLabel));
        log.info("=== end init label maps");
    }
}
