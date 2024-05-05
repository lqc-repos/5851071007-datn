package thesis.core.search_engine.service;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.article.Article;
import thesis.core.article.command.CommandCommonQuery;
import thesis.core.article.model.author.Author;
import thesis.core.article.model.label.custom_label.CustomLabel;
import thesis.core.article.model.label.loc_label.LOCLabel;
import thesis.core.article.model.label.nlp_label.NLPLabel;
import thesis.core.article.model.label.org_label.ORGLabel;
import thesis.core.article.model.label.per_label.PERLabel;
import thesis.core.article.model.topic.Topic;
import thesis.core.article.service.ArticleService;
import thesis.core.nlp.dto.AnnotatedWord;
import thesis.core.nlp.service.NLPService;
import thesis.core.search_engine.SearchEngine;
import thesis.core.search_engine.command.CommandSearchArticle;
import thesis.core.search_engine.dto.SearchEngineResult;

import java.util.*;

@Service
public class SearchEngineServiceImp implements SearchEngineService {
    @Autowired
    private NLPService nlpService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private SearchEngine searchEngine;

    @Override
    public Optional<SearchEngineResult> searchArticle(CommandSearchArticle command) throws Exception {
        if (StringUtils.isBlank(command.getSearch()))
            return Optional.empty();
        Optional<AnnotatedWord> annotatedWordOptional = nlpService.annotateSearch(command.getSearch());
        if (annotatedWordOptional.isEmpty())
            throw new Exception(String.format("Can not annotate the search text \"%s\"", command.getSearch()));
        AnnotatedWord annotatedWord = annotatedWordOptional.get();
        Set<String> articleIds = new HashSet<>();

        if (annotatedWord.getTopic() != null) {
            Topic topic = searchEngine.getTopicMap().get(annotatedWord.getTopic().getVnValue().replace(" ", "_"));
            articleIds.addAll(topic.getArticleIds());
        }
        for (AnnotatedWord.TaggedWord word : annotatedWord.getTaggedWords()) {
            if (searchEngine.getStopWords().contains(word.getWord()))
                continue;
            switch (word.getLabelType()) {
                case AUTHOR -> {
                    Author author = searchEngine.getAuthorMap().get(word.getWord());
                    if (author != null)
                        articleIds.addAll(author.getArticleIds());
                }
                case PER -> {
                    PERLabel perLabel = searchEngine.getPerLabelMap().get(word.getWord());
                    if (perLabel != null)
                        articleIds.addAll(perLabel.getArticleIds());
                }
                case ORG -> {
                    ORGLabel orgLabel = searchEngine.getOrgLabelMap().get(word.getWord());
                    if (orgLabel != null)
                        articleIds.addAll(orgLabel.getArticleIds());
                }
                case LOC -> {
                    LOCLabel locLabel = searchEngine.getLocLabelMap().get(word.getWord());
                    if (locLabel != null)
                        articleIds.addAll(locLabel.getArticleIds());
                }
                case UND -> {
                    CustomLabel customLabel = searchEngine.getCustomLabelMap().get(word.getWord());
                    if (customLabel != null)
                        articleIds.addAll(customLabel.getArticleIds());
                    NLPLabel nlpLabel = searchEngine.getNlpLabelMap().get(word.getWord());
                    if (nlpLabel != null)
                        articleIds.addAll(nlpLabel.getArticleIds());
                }
            }
        }

        if (CollectionUtils.isEmpty(articleIds))
            return Optional.of(SearchEngineResult.builder()
                    .search(command.getSearch())
                    .articles(new ArrayList<>())
                    .total(0L)
                    .totalPage(0)
                    .page(command.getPage())
                    .size(command.getSize())
                    .build());

        long totalArticle = articleService.count(CommandCommonQuery.builder()
                .articleIds(articleIds)
                .build()).orElse(0L);
        List<Article> articles = articleService.getMany(CommandCommonQuery.builder()
                .articleIds(articleIds)
                .page(command.getPage())
                .size(command.getSize())
                .build());

        return Optional.of(SearchEngineResult.builder()
                .search(command.getSearch())
                .articles(articles)
                .total(totalArticle)
                .totalPage((int) ((totalArticle + command.getSize() - 1) / command.getSize()))
                .page(command.getPage())
                .size(command.getSize())
                .build());
    }
}
