package thesis.crawler;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.article.Article;
import thesis.article.application.ArticleApplicationImp;
import thesis.command.CommandCrawlArticle;
import thesis.constant.VNExpressConst;
import thesis.error_article.ErrorArticle;
import thesis.error_article.application.ErrorArticleApplicationImp;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
public class VNExpressArticleCrawler implements ArticleCrawler {
    @Autowired
    private ArticleApplicationImp articleApplication;
    @Autowired
    private ErrorArticleApplicationImp errorArticleApplicationImp;

    @Override
    public List<Article> crawl(CommandCrawlArticle command) {
        List<String> urls = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(command.getUrls()))
            urls.addAll(command.getUrls());
        if (CollectionUtils.isNotEmpty(command.getTopics()))
            urls.addAll(getUrlsByTopics(command.getTopics()));
        if (CollectionUtils.isEmpty(urls))
            return Collections.emptyList();
        verifyUrl(urls);
        log.info("Count: {} - Urls to crawl: {}", urls.size(), urls);
        List<Article> articles = urls.stream().map(this::crawl).filter(Objects::nonNull).toList();
        if (CollectionUtils.isNotEmpty(articles))
            articleApplication.addMany(articles);
        return articles;
    }

    @Override
    public Optional<Long> crawlAll(CommandCrawlArticle command) {
        AtomicLong count = new AtomicLong(0L);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int i = 0; i < ObjectUtils.defaultIfNull(command.getPage(), VNExpressConst.PAGE_MAX); i++) {
            for (String topic : VNExpressConst.TOPIC.getAllValues()) {
                final int page = i + 1;
                log.info("Page: {} - Topic: {}", page, topic);
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    List<Article> articles = crawl(CommandCrawlArticle.builder()
                            .topics(Collections.singletonList(
                                    CommandCrawlArticle.Topic.builder()
                                            .pages(Collections.singletonList(page))
                                            .name(topic)
                                            .build()
                            ))
                            .build());
                    count.getAndAdd(articles.size());
                });
                futures.add(future);
            }
        }
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.join();
        return Optional.of(count.get());
    }

    private Article crawl(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            String title = find(document, VNExpressConst.CSS_QUERY.TITLE.getValue()).text();
            String content = String.join("\n", find(document,
                    VNExpressConst.CSS_QUERY.CONTENT_WITHOUT_AUTHOR.getValue()).eachText());
            if (StringUtils.isAnyBlank(title, content))
                throw new Exception("Title or content is empty");
            String location = StringUtils.defaultIfBlank(find(document, VNExpressConst.CSS_QUERY.LOCATION.getValue()).text(), null);
//            String description = Optional.ofNullable(find(document, VNExpressConst.CSS_QUERY.DESCRIPTION.getValue()).first()).map(Element::ownText)
//                    .orElse(find(document, VNExpressConst.CSS_QUERY.DESCRIPTION.getValue()).text());
            String description = find(document, VNExpressConst.CSS_QUERY.DESCRIPTION.getValue()).first().ownText();
            List<String> authors = Arrays.stream(find(document,
                            VNExpressConst.CSS_QUERY.AUTHOR.getValue()).text().split("[-–]"))
                    .map(String::trim).collect(Collectors.toList());
            List<String> topics = Collections.singletonList(
                    StringUtils.defaultIfBlank(
                            find(document, VNExpressConst.CSS_QUERY.TOPIC.getValue()).first().text(),
                            find(document, VNExpressConst.CSS_QUERY.TOPIC.getValue()).first().attr("title")));
            List<String> labels = new ArrayList<>();
            String hotLabel = find(document, VNExpressConst.CSS_QUERY.HOT_LABEL.getValue()).text();
            List<String> tags = Arrays.stream(find(document, VNExpressConst.CSS_QUERY.TAG.getValue()).attr("content")
                            .split(","))
                    .map(String::trim)
                    .toList();
            if (StringUtils.isNotBlank(hotLabel))
                labels.add(hotLabel);
            if (CollectionUtils.isNotEmpty(tags))
                labels.addAll(tags);
            return Article.builder()
                    .title(title)
                    .location(location)
                    .description(description)
                    .content(content)
                    .authors(authors)
                    .topics(topics)
                    .labels(labels)
                    .url(url)
                    .build();
        } catch (Exception ex) {
            log.warn("Cannot get article url: {}, reason: {}", url, ex.getMessage());
            errorArticleApplicationImp.addIfNotExist(ErrorArticle.builder()
                    .url(url)
                    .reason(ex.getMessage())
                    .build());
        }
        return null;
    }

    private List<String> getUrlsByTopics(List<CommandCrawlArticle.Topic> topics) {
        return topics.parallelStream().flatMap(topic -> {
            if (StringUtils.isBlank(topic.getName()) || CollectionUtils.isEmpty(topic.getPages()))
                return Stream.empty();
            return topic.getPages().parallelStream().flatMap(page -> {
                try {
                    Document document = Jsoup.connect(String.format(VNExpressConst.TOPIC_URL_FORMATTER,
                            topic.getName(), page)).get();
                    Elements titles = find(document, ".title-news");
                    if (CollectionUtils.isEmpty(titles))
                        return Stream.empty();
                    return titles.stream().map(title -> {
                        Element element = title.select("a").first();
                        return element.attr("href");
                    }).filter(Objects::nonNull);
                } catch (Exception ex) {
                    log.warn(ex.getMessage(), ex);
                }
                return Stream.empty();
            });
        }).collect(Collectors.toList());
    }

    private void verifyUrl(List<String> originUrls) {
        originUrls.removeIf(url -> url.contains("https://video."));
        List<Article> articles = articleApplication.findByUrls(originUrls);
        if (CollectionUtils.isNotEmpty(articles)) {
            List<String> existedUrls = articles.stream().map(Article::getUrl).toList();
            originUrls.removeIf(existedUrls::contains);
        }
        List<ErrorArticle> errorArticles = errorArticleApplicationImp.findByUrls(originUrls);
        if (CollectionUtils.isNotEmpty(errorArticles)) {
            List<String> existedUrls = errorArticles.stream().map(ErrorArticle::getUrl).toList();
            originUrls.removeIf(existedUrls::contains);
        }
    }
}
