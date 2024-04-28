package thesis.core.crawler.crawled_article.service;

import thesis.core.crawler.crawled_article.CrawledArticle;
import thesis.core.crawler.crawled_article.command.CommandQueryCrawledArticle;

import java.util.List;
import java.util.Optional;

public interface CrawledArticleService {

    Optional<Long> count(CommandQueryCrawledArticle command);

    List<CrawledArticle> getMany(CommandQueryCrawledArticle command);

    List<CrawledArticle> getByUrls(List<String> urls);

    Optional<Boolean> add(CrawledArticle crawledArticle);

    Optional<Boolean> addMany(List<CrawledArticle> crawledArticles);
}
