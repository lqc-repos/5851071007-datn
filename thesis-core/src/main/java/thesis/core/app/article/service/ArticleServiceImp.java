package thesis.core.app.article.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.app.article.repository.ArticleRepositoryImp;

@Service
public class ArticleServiceImp implements ArticleService {
    @Autowired
    private ArticleRepositoryImp articleRepository;
}
