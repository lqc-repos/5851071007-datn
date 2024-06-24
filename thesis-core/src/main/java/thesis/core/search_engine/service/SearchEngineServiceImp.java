package thesis.core.search_engine.service;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
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
import thesis.core.news.report.news_report.NewsReport;
import thesis.core.news.report.news_report.repository.NewsReportRepository;
import thesis.core.nlp.dto.AnnotatedWord;
import thesis.core.nlp.service.NLPService;
import thesis.core.search_engine.SearchEngine;
import thesis.core.search_engine.command.CommandSearchArticle;
import thesis.core.search_engine.dto.SearchEngineResult;
import thesis.utils.constant.REPORT_TYPE;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class SearchEngineServiceImp implements SearchEngineService {
    private static final ZoneOffset ZONE_OFFSET = ZoneOffset.of("+07:00");

    @Autowired
    private NLPService nlpService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private SearchEngine searchEngine;
    @Autowired
    private NewsReportRepository newsReportRepository;

    @Override
    public Optional<SearchEngineResult> searchArticle(CommandSearchArticle command) throws Exception {
        if (StringUtils.isBlank(command.getSearch()))
            return Optional.empty();
        Optional<AnnotatedWord> annotatedWordOptional = nlpService.annotateSearch(command.getSearch());
        if (annotatedWordOptional.isEmpty())
            throw new Exception(String.format("Can not annotate the search text \"%s\"", command.getSearch()));
        AnnotatedWord annotatedWord = annotatedWordOptional.get();
        Map<String, Long> articleIdSearchMap = new HashMap<>();

        Set<String> labelsToReport = new HashSet<>();

        if (BooleanUtils.isTrue(command.getIsCustomTag())) {
            for (String articleId : getArticleIdsFromCustomLabel(command.getSearch())) {
                articleIdSearchMap.put(articleId, 1L);
            }
        } else {
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
                    labelsToReport.add(word.getWord());
                    long labelScore = Optional.ofNullable(searchEngine.getLabelScoreMap().get(word.getLabelType().getValue()))
                            .orElse(0L);
                    for (String articleId : articleIds) {
                        articleIdSearchMap.put(articleId, articleIdSearchMap.getOrDefault(articleId, 0L) + labelScore);
                    }
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
                .page(1)
                .size(command.getSize())
                .build());
        for (Article article : articles) {
            articlePerPage.put(article.getId().toString(), article);
        }

        if (command.getPage() <= 1) {
            CompletableFuture.runAsync(() -> processReportedLabel(labelsToReport));
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

    @Override
    public Optional<SearchEngineResult> searchArticleByTopic(CommandSearchArticle command) throws Exception {
        long totalArticle = articleService.count(CommandCommonQuery.builder()
                .topics(Set.of(command.getTopic()))
                .build()).orElse(0L);
        List<Article> articles = articleService.get(CommandCommonQuery.builder()
                .isDescCreatedDate(true)
                .isDescPublicationDate(true)
                .page(command.getPage())
                .size(command.getSize())
                .topics(Set.of(command.getTopic()))
                .build());
        return Optional.of(SearchEngineResult.builder()
                .search(command.getSearch())
                .articles(new LinkedList<>(articles))
                .topic(command.getTopic())
                .total(totalArticle)
                .totalPage((int) ((totalArticle + command.getSize() - 1) / command.getSize()))
                .page(command.getPage())
                .size(command.getSize())
                .build());
    }

    private Set<String> getArticleIdsFromCustomLabel(String search) {
        String label = search.replaceAll("\\s+", "_").toLowerCase();
        CustomLabel customLabel = searchEngine.getCustomLabelMap().get(label);
        if (customLabel != null && CollectionUtils.isNotEmpty(customLabel.getArticleIds()))
            return customLabel.getArticleIds();
        return Collections.emptySet();
    }

    private void processReportedLabel(Set<String> labels) {
        long currentTime = System.currentTimeMillis() / 1000;
        long startOfDay = LocalDateTime.ofEpochSecond(currentTime, 0, ZONE_OFFSET).toLocalDate()
                .atStartOfDay().toEpochSecond(ZONE_OFFSET);

        if (CollectionUtils.isEmpty(labels))
            return;

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("reportDate", startOfDay);
        queryMap.put("reportType", REPORT_TYPE.LABEL.getValue());
        Map<String, Object> sortMap = new HashMap<>();
        sortMap.put("updatedDate", -1);
        sortMap.put("createdDate", -1);

        NewsReport newsReport = newsReportRepository.findOne(queryMap, sortMap).orElse(null);
        if (newsReport == null) {
            newsReport = NewsReport.builder()
                    .reportDate(startOfDay)
                    .reportType(REPORT_TYPE.LABEL.getValue())
                    .labelCounts(new HashMap<>())
                    .build();
            newsReportRepository.insert(newsReport);
        }

        for (String label : labels) {
            newsReport.getLabelCounts().compute(label, (k, v) -> (v == null) ? 1 : v + 1);
        }

        newsReportRepository.update(new Document("_id", newsReport.getId()), new Document("labelCounts", newsReport.getLabelCounts()));
    }
}