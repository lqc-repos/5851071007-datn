package thesis.crawler;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@Log4j2
public class VNExpressArticleCrawler implements ArticleCrawler {
    private static final long ONE_MONTH_IN_SECOND = 2629743L;
    private static final long FIRST_DAY_OF_2023_IN_SECOND = 1672531200L;

    @Autowired
    private ArticleApplicationImp articleApplication;
    @Autowired
    private ErrorArticleApplicationImp errorArticleApplicationImp;

    @Override
    public List<Article> crawl(CommandCrawlArticle command) {
        List<String> urls = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(command.getUrls()))
            urls.addAll(command.getUrls());
        if (StringUtils.isNotBlank(command.getTopic()))
            urls.addAll(getUrlsByTopics(command));
        if (StringUtils.isNotBlank(command.getCategory())
                && command.getFromDate() != null
                && command.getToDate() != null)
            urls.addAll(getUrlsBySearch(command));
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
    public Optional<Long> crawlByTopic() {
        AtomicLong count = new AtomicLong(0L);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int i = 0; i < VNExpressConst.PAGE_MAX; i++) {
            for (String topic : VNExpressConst.TOPIC.getValues()) {
                final int page = i + 1;
                log.info("Page: {} - Topic: {}", page, topic);
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    List<Article> articles = crawl(CommandCrawlArticle.builder()
                            .topic(topic)
                            .page(page)
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

    @Override
    public Optional<Long> crawlBySearch() {
        AtomicLong count = new AtomicLong(0L);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        long toDate = System.currentTimeMillis() / 1000;
        long fromDate = toDate - ONE_MONTH_IN_SECOND;
        while (fromDate >= FIRST_DAY_OF_2023_IN_SECOND) {
            for (String category : VNExpressConst.TOPIC.getCategoryIds()) {
                final int page = new Random().nextInt(20) + 1;
                long finalFromDate = fromDate;
                long finalToDate = toDate;
                log.info("Page: {} - category: {} - fromDate: {} - toDate: {}", page, category, fromDate, toDate);
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    List<Article> articles = crawl(CommandCrawlArticle.builder()
                            .category(category)
                            .fromDate(finalFromDate)
                            .toDate(finalToDate)
                            .page(page)
                            .build());
                    count.getAndAdd(articles.size());
                });
                futures.add(future);
            }
            toDate = fromDate;
            fromDate = toDate - ONE_MONTH_IN_SECOND;
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
            Long publicationTime = getPublicationTime(find(document, VNExpressConst.CSS_QUERY.PUBLICATION_DATE.getValue()).text());
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
                    .publicationDate(publicationTime)
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

    private List<String> getUrlsByTopics(CommandCrawlArticle command) {
        if (StringUtils.isBlank(command.getTopic()) || command.getPage() == null)
            return Collections.emptyList();
        try {
            Document document = Jsoup.connect(String.format(VNExpressConst.TOPIC_URL_FORMATTER,
                    command.getTopic(), command.getPage())).get();
            return crawlUrls(document);
        } catch (Exception ex) {
            log.warn(ex.getMessage(), ex);
        }
        return Collections.emptyList();
    }

    private List<String> getUrlsBySearch(CommandCrawlArticle command) {
        if (StringUtils.isBlank(command.getCategory())
                || command.getFromDate() == null || command.getToDate() == null || command.getPage() == null)
            return Collections.emptyList();
        try {
            String url = String.format(VNExpressConst.CATEGORY_URL_FORMATTER,
                    command.getCategory(), command.getFromDate(), command.getToDate(), command.getPage());
            log.info("--- Url: {}", url);
            Document document = Jsoup.connect(url).get();
            return crawlUrls(document);
        } catch (Exception ex) {
            log.warn(ex.getMessage(), ex);
        }
        return Collections.emptyList();
    }

    private List<String> crawlUrls(Document document) {
        Elements titles = find(document, ".title-news");
        if (CollectionUtils.isEmpty(titles))
            return Collections.emptyList();
        return titles.stream().map(title -> {
            Element element = title.select("a").first();
            return element.attr("href");
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private Long getPublicationTime(String timeStr) {
        timeStr = timeStr.substring(timeStr.indexOf(",") + 1, timeStr.indexOf("(")).trim();
        LocalDateTime localDateTime = LocalDateTime.parse(timeStr, VNExpressConst.dateTimeFormatter);
        return localDateTime.atZone(ZoneId.of("GMT+7")).toInstant().toEpochMilli();
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
