package thesis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @RequestMapping(method = RequestMethod.GET, value = "/crawl-all-article")
    public ResponseEntity<Long> crawlAllArticle(@RequestParam(required = false) Integer page) {
        return new ResponseEntity<>(vnExpressArticleCrawler.crawlAll(
                CommandCrawlArticle.builder()
                        .page(page)
                        .build()).orElse(0L),
                HttpStatus.OK);
    }
}
