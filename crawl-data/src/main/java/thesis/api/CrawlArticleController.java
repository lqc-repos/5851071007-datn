package thesis.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import thesis.core.crawler.crawled_article.CrawledArticle;
import thesis.core.crawler.crawled_article.command.CommandCrawlArticle;
import thesis.core.crawler.crawled_article.service.ArticleCrawlerService;

import java.util.List;

@RestController
public class CrawlArticleController {
    @Autowired
    private ArticleCrawlerService articleCrawlerService;

    @RequestMapping(method = RequestMethod.POST, value = "/crawl-article")
    public ResponseEntity<List<CrawledArticle>> crawlArticle(@RequestBody CommandCrawlArticle command) {
        return new ResponseEntity<>(articleCrawlerService.crawl(command), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/crawl-by-topic")
    public ResponseEntity<Long> crawlByTopic() {
        return new ResponseEntity<>(articleCrawlerService.crawlByTopic().orElse(0L), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/crawl-by-search")
    public ResponseEntity<Boolean> crawlBySearch() {
        return new ResponseEntity<>(articleCrawlerService.crawlBySearch().orElse(Boolean.FALSE), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/crawl-images")
    public ResponseEntity<Boolean> crawlImages() {
        return new ResponseEntity<>(articleCrawlerService.crawlImages().orElse(Boolean.FALSE), HttpStatus.OK);
    }
}
