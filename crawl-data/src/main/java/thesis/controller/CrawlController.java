package thesis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import thesis.article.Article;
import thesis.command.CommandCrawlArticle;
import thesis.crawler.VNExpressArticleCrawler;

import java.util.List;

@RestController
public class CrawlController {
    @Autowired
    private VNExpressArticleCrawler vnExpressArticleCrawler;

    @RequestMapping(method = RequestMethod.POST, value = "/crawl-article")
    public ResponseEntity<List<Article>> crawlArticle(@RequestBody CommandCrawlArticle command) {
        return new ResponseEntity<>(vnExpressArticleCrawler.crawl(command), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/crawl-by-topic")
    public ResponseEntity<Long> crawlByTopic() {
        return new ResponseEntity<>(vnExpressArticleCrawler.crawlByTopic().orElse(0L), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/crawl-by-search")
    public ResponseEntity<Boolean> crawlBySearch() {
        return new ResponseEntity<>(vnExpressArticleCrawler.crawlBySearch().orElse(Boolean.FALSE), HttpStatus.OK);
    }
}
