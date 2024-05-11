package thesis.core.article.service;

import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.article.Article;
import thesis.core.article.command.CommandCommonQuery;
import thesis.core.article.repository.ArticleRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImp implements ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public Optional<Long> count(CommandCommonQuery command) {
        Map<String, Object> query = new HashMap<>();
        if (CollectionUtils.isNotEmpty(command.getArticleIds()))
            query.put("_id", new Document("$in",
                    command.getArticleIds().stream().map(ObjectId::new).collect(Collectors.toList())));
        return articleRepository.count(new Document(query));
    }

    @Override
    public List<Article> get(CommandCommonQuery command) {
        Map<String, Object> queryMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(command.getArticleIds()))
            queryMap.put("_id", new Document("$in",
                    command.getArticleIds().stream().map(ObjectId::new).collect(Collectors.toList())));

        if (CollectionUtils.isNotEmpty(command.getTopics()))
            queryMap.put("topic", new Document("$in",
                    command.getTopics().stream().map(ObjectId::new).collect(Collectors.toList())));

        Map<String, Object> sortMap = new HashMap<>();
        if (command.getIsDescId() != null)
            sortMap.put("_id", command.getIsDescId() ? -1 : 1);
        if (command.getIsDescCreatedDate() != null)
            sortMap.put("createdDate", command.getIsDescCreatedDate() ? -1 : 1);
        if (command.getIsDescPublicationDate() != null)
            sortMap.put("publicationDate", command.getIsDescPublicationDate() ? -1 : 1);

        Map<String, Object> projectionMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(command.getProjections())) {
            for (String projection : command.getProjections()) {
                projectionMap.put(projection, "1");
            }
        }
        return articleRepository.find(queryMap, sortMap, projectionMap,
                (command.getPage() - 1) * command.getSize(), command.getSize());
    }

    @Override
    public Optional<Boolean> add(Article article) {
        return articleRepository.insert(article);
    }

    @Override
    public Optional<Boolean> addMany(List<Article> articles) {
        return articleRepository.insertMany(articles);
    }
}
