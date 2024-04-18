package thesis.crawler;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import thesis.article.Article;
import thesis.command.CommandCrawlArticle;

import java.util.List;
import java.util.Optional;

public interface ArticleCrawler {
    List<Article> crawl(CommandCrawlArticle command);

    Optional<Long> crawlAll(CommandCrawlArticle command);

    default Elements find(Document document, String cssQuery) {
        return document.select(cssQuery);
    }
}
