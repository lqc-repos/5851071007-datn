package thesis.core.article.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.article.Article;
import thesis.core.article.command.CommandCommonQuery;
import thesis.core.article.repository.ArticleRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ArticleServiceImp implements ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public Optional<Long> count(CommandCommonQuery command) {
        return articleRepository.count(new Document());
    }

    @Override
    public List<Article> getMany(CommandCommonQuery command) {
        Map<String, Object> sort = new HashMap<>();
        if (command.getIsDescId() != null)
            sort.put("_id", command.getIsDescId() ? -1 : 1);
        if (command.getIsDescCreatedDate() != null)
            sort.put("createdDate", command.getIsDescCreatedDate() ? -1 : 1);
        if (command.getIsDescPublicationDate() != null)
            sort.put("publicationDate", command.getIsDescPublicationDate() ? -1 : 1);
        return articleRepository.find(new Document(), sort, (command.getPage() - 1) * command.getSize(), command.getSize());
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
