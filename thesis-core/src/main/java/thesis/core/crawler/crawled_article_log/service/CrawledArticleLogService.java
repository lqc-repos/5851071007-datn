package thesis.core.crawler.crawled_article_log.service;

import thesis.core.crawler.crawled_article_log.CrawledArticleLog;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CrawledArticleLogService {
    Optional<Boolean> add(CrawledArticleLog crawledArticleLog);
}
