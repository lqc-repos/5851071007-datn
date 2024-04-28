package thesis.core.crawler.crawled_article.repository;

import thesis.core.crawler.crawled_article.CrawledArticle;
import thesis.utils.mongo.repo.MongoDBRepository;

public interface CrawledArticleRepository extends MongoDBRepository<CrawledArticle> {
}
