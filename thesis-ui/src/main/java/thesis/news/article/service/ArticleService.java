package thesis.news.article.service;

import thesis.news.article.dto.Article;

import java.util.List;

public interface ArticleService {
    Article getArticleById(String id) throws Exception;

    List<Article> getLatestNews() throws Exception;
}
