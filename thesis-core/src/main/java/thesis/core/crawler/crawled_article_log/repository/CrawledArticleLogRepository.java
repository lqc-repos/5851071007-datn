package thesis.core.crawler.crawled_article_log.repository;

import thesis.core.crawler.crawled_article_log.CrawledArticleLog;
import thesis.utils.mongo.repo.MongoDBRepository;

public interface CrawledArticleLogRepository extends MongoDBRepository<CrawledArticleLog> {
}
