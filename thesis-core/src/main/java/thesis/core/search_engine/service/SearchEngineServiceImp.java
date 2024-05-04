package thesis.core.search_engine.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.article.Article;
import thesis.core.article.command.CommandCommonQuery;
import thesis.core.article.model.label.per_label.PERLabel;
import thesis.core.article.service.ArticleService;
import thesis.core.nlp.dto.AnnotatedWord;
import thesis.core.nlp.service.NLPService;
import thesis.core.search_engine.SearchEngine;
import thesis.core.search_engine.command.CommandSearchArticle;
import thesis.core.search_engine.dto.SearchEngineResult;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        Optional<AnnotatedWord> annotatedWordOptional = nlpService.annotate(command.getSearch());
        if (annotatedWordOptional.isEmpty())
            throw new Exception(String.format("Can not annotate the search text \"%s\"", command.getSearch()));
        Set<String> articleIds = new HashSet<>();
        for (AnnotatedWord.TaggedWord word : annotatedWordOptional.get().getTaggedWords()) {
            String label = word.getWord().toLowerCase().trim();
            switch (word.getNer()) {
                case "B-PER":
                case "I-PER":
                    PERLabel perLabel = searchEngine.getPerLabelMap().get(label);
                    if (perLabel != null) {
                        articleIds.addAll(perLabel.getArticleIds());
                    }
                    break;
                case "B-ORG":
                case "I-ORG":
                    break;
                case "B-LOC":
                case "I-LOC":
                    break;
                default:
                    break;
            }
        }
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
