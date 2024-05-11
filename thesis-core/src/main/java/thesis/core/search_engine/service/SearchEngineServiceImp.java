package thesis.core.search_engine.service;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
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
import java.util.stream.Collectors;

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
        Map<String, Long> articleIdSearchMap = new HashMap<>();

        for (AnnotatedWord.TaggedWord word : annotatedWord.getTaggedWords()) {
            if (searchEngine.getStopWords().contains(word.getWord()))
                continue;
            String formattedWord = word.getWord().toLowerCase();
            Set<String> articleIds = new HashSet<>();
            switch (word.getLabelType()) {
                case AUTHOR -> {
                    Author author = searchEngine.getAuthorMap().get(formattedWord);
                    if (author != null && CollectionUtils.isNotEmpty(author.getArticleIds())) {
                        articleIds.addAll(author.getArticleIds());
                    }
                }
                case TOPIC -> {
                    Topic topic = searchEngine.getTopicMap().get(formattedWord);
                    if (topic != null && CollectionUtils.isNotEmpty(topic.getArticleIds())) {
                        articleIds.addAll(topic.getArticleIds());
                    }
                }
                case PER -> {
                    PERLabel perLabel = searchEngine.getPerLabelMap().get(formattedWord);
                    if (perLabel != null && CollectionUtils.isNotEmpty(perLabel.getArticleIds())) {
                        articleIds.addAll(perLabel.getArticleIds());
                    }
                }
                case ORG -> {
                    ORGLabel orgLabel = searchEngine.getOrgLabelMap().get(formattedWord);
                    if (orgLabel != null && CollectionUtils.isNotEmpty(orgLabel.getArticleIds())) {
                        articleIds.addAll(orgLabel.getArticleIds());
                    }
                }
                case LOC -> {
                    LOCLabel locLabel = searchEngine.getLocLabelMap().get(formattedWord);
                    if (locLabel != null && CollectionUtils.isNotEmpty(locLabel.getArticleIds())) {
                        articleIds.addAll(locLabel.getArticleIds());
                    }
                }
                case UND -> {
                    CustomLabel customLabel = searchEngine.getCustomLabelMap().get(formattedWord);
                    if (customLabel != null && CollectionUtils.isNotEmpty(customLabel.getArticleIds())) {
                        articleIds.addAll(customLabel.getArticleIds());
                        break;
                    }
                    NLPLabel nlpLabel = searchEngine.getNlpLabelMap().get(formattedWord);
                    if (nlpLabel != null && CollectionUtils.isNotEmpty(nlpLabel.getArticleIds())) {
                        articleIds.addAll(nlpLabel.getArticleIds());
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(articleIds)) {
                long labelScore = Optional.ofNullable(searchEngine.getLabelScoreMap().get(word.getLabelType().getValue()))
                        .orElse(0L);
                for (String articleId : articleIds) {
                    articleIdSearchMap.put(articleId, articleIdSearchMap.getOrDefault(articleId, 0L) + labelScore);
                }
            }
        }

        if (MapUtils.isEmpty(articleIdSearchMap))
            return Optional.of(SearchEngineResult.builder()
                    .search(command.getSearch())
                    .articles(new ArrayList<>())
                    .total(0L)
                    .totalPage(0)
                    .page(command.getPage())
                    .size(command.getSize())
                    .build());
        Set<String> articleIds = articleIdSearchMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        long totalArticle = articleService.count(CommandCommonQuery.builder()
                .articleIds(articleIds)
                .build()).orElse(0L);
        LinkedHashMap<String, Article> articlePerPage = new LinkedHashMap<>();

        int skip = (command.getPage() - 1) * command.getSize();
        articleIds.stream().skip(skip).limit(command.getSize()).collect(Collectors.toCollection(LinkedHashSet::new))
                .forEach(id -> articlePerPage.put(id, null));

        List<Article> articles = articleService.get(CommandCommonQuery.builder()
                .articleIds(articlePerPage.keySet())
                .page(command.getPage())
                .size(command.getSize())
                .build());
        for (Article article : articles) {
            articlePerPage.put(article.getId().toString(), article);
        }
        return Optional.of(SearchEngineResult.builder()
                .search(command.getSearch())
                .articles(new LinkedList<>(articlePerPage.values()))
                .total(totalArticle)
                .totalPage((int) ((totalArticle + command.getSize() - 1) / command.getSize()))
                .page(command.getPage())
                .size(command.getSize())
                .build());
    }
}
