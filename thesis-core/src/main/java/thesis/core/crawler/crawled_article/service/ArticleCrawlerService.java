package thesis.core.crawler.crawled_article.service;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import thesis.core.crawler.crawled_article.CrawledArticle;
import thesis.core.crawler.crawled_article.command.CommandCrawlArticle;

import java.util.List;
import java.util.Optional;

public interface ArticleCrawlerService {
    List<CrawledArticle> crawl(CommandCrawlArticle command);

    Optional<Long> crawlByTopic();

    Optional<Boolean> crawlBySearch();
    
    Optional<Boolean> crawlImages();

    default Elements find(Document document, String cssQuery) {
        return document.select(cssQuery);
    }
}
