package thesis.core.crawler.crawled_article.service;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.crawler.crawled_article.CrawledArticle;
import thesis.core.crawler.crawled_article.command.CommandCrawlArticle;
import thesis.core.crawler.crawled_article.command.CommandQueryCrawledArticle;
import thesis.core.crawler.crawled_article.repository.CrawledArticleRepository;
import thesis.core.crawler.crawled_article_error.CrawledArticleError;
import thesis.core.crawler.crawled_article_error.service.CrawledArticleErrorService;
import thesis.core.crawler.crawled_article_log.CrawledArticleLog;
import thesis.core.crawler.crawled_article_log.service.CrawledArticleLogService;
import thesis.utils.constant.VNExpressConst;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Log4j2
public class ArticleCrawlerServiceImp implements ArticleCrawlerService {
    private static final LocalDateTime CURRENT_CRAWLED_DATE = LocalDateTime.now().with(LocalTime.MAX);
    private static final LocalDateTime MIN_CRAWLED_DATE = LocalDateTime.now().minusYears(2).withDayOfMonth(1).withMonth(1).with(LocalTime.MIN);
    private static final ZoneId zoneId = ZoneId.of("GMT+7");

    @Autowired
    private CrawledArticleService crawledArticleService;
    @Autowired
    private CrawledArticleErrorService crawledArticleErrorService;
    @Autowired
    private CrawledArticleLogService crawledArticleLogService;
    @Autowired
    private CrawledArticleRepository crawledArticleRepository;

    @Override
    public List<CrawledArticle> crawl(CommandCrawlArticle command) {
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
        List<CrawledArticle> crawledArticles = urls.stream().map(this::crawl).filter(Objects::nonNull).toList();
        if (CollectionUtils.isNotEmpty(crawledArticles))
            crawledArticleService.addMany(crawledArticles);
        return crawledArticles;
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
                    List<CrawledArticle> crawledArticles = crawl(CommandCrawlArticle.builder()
                            .topic(topic)
                            .page(page)
                            .build());
                    count.getAndAdd(crawledArticles.size());
                });
                futures.add(future);
            }
        }
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.join();
        return Optional.of(count.get());
    }

    @Override
    public Optional<Boolean> crawlBySearch() {
        LocalDateTime toDate = CURRENT_CRAWLED_DATE;
        LocalDateTime fromDate = toDate.minusMonths(1);
        while (MIN_CRAWLED_DATE.isBefore(fromDate)) {
            final LocalDateTime finalFromDate = fromDate;
            final LocalDateTime finalToDate = toDate;
            for (String category : VNExpressConst.TOPIC.getCategoryIds()) {
                IntStream.generate(() -> ThreadLocalRandom.current().nextInt(1, 21))
                        .distinct()
                        .limit(5)
                        .forEach(page -> {
//                            log.info("Page: {} - category: {} - fromDate: {} - toDate: {}", page, category, finalFromDate, finalToDate);
                            crawledArticleLogService.add(CrawledArticleLog.builder()
                                    .crawledDate(CURRENT_CRAWLED_DATE.atZone(zoneId).toEpochSecond())
                                    .category(category)
                                    .fromDate(finalFromDate.atZone(zoneId).toEpochSecond())
                                    .toDate(finalToDate.atZone(zoneId).toEpochSecond())
                                    .page(page)
                                    .build());
                            CompletableFuture.runAsync(() -> {
                                List<CrawledArticle> crawledArticles = crawl(CommandCrawlArticle.builder()
                                        .category(category)
                                        .fromDate(finalFromDate.atZone(zoneId).toEpochSecond())
                                        .toDate(finalToDate.atZone(zoneId).toEpochSecond())
                                        .page(page)
                                        .build());
                            });
                        });
            }
            toDate = fromDate;
            fromDate = toDate.minusMonths(1);
        }
        return Optional.of(Boolean.TRUE);
    }

    @Override
    public Optional<Boolean> crawlImages() {
        ExecutorService executor = Executors.newFixedThreadPool(20);
        Set<String> urls = new HashSet<>();
        Long totalCrawledArticle = crawledArticleService.count(CommandQueryCrawledArticle.builder().build()).orElseThrow();
        int sizePerPage = 100, totalPage = (int) ((totalCrawledArticle + sizePerPage - 1) / sizePerPage);
        log.info("------ Total page: {}", totalPage);
        for (int i = 0; i < totalPage; i++) {
            int finalI = i;
            CompletableFuture.runAsync(() -> {
                List<CrawledArticle> crawledArticles = crawledArticleService.getMany(CommandQueryCrawledArticle.builder()
                        .isDescPublicationDate(false)
                        .page(finalI)
                        .size(sizePerPage)
                        .build());
                log.info("-----Start Page: {} - Size: {}", finalI, crawledArticles.size());
                for (CrawledArticle crawledArticle : crawledArticles) {
                    if (urls.contains(crawledArticle.getUrl()))
                        continue;
                    if (CollectionUtils.isNotEmpty(crawledArticle.getImages()))
                        continue;
                    try {
                        String url = crawledArticle.getUrl();
                        Document document = Jsoup.connect(url).get();

                        LinkedList<CrawledArticle.Image> images = new LinkedList<>();

                        LinkedList<String> imageDescriptions = new LinkedList<>();
                        for (Element imageElement : find(document, VNExpressConst.CSS_QUERY.IMAGE_DESCRIPTION.getValue())) {
                            if (imageElement != null) {
                                imageDescriptions.add(imageElement.text());
                            } else {
                                System.out.println("No image description element found");
                            }
                        }
                        int count = 0;
                        for (Element imageElement : find(document, VNExpressConst.CSS_QUERY.POSTER_URL.getValue())) {
                            if (imageElement != null) {
                                Element source = imageElement.selectFirst("source[data-srcset]");
                                if (source != null) {
                                    String dataSrcset = source.attr("data-srcset");
                                    String[] imgUrls = dataSrcset.split(", ");
                                    for (String imgUrl : imgUrls) {
                                        String[] parts = imgUrl.split(" ");
                                        if (parts.length == 2 && parts[1].equals("1x")) {
                                            String imageUrl = parts[0];
                                            images.add(CrawledArticle.Image.builder()
                                                    .url(imageUrl)
                                                    .description(imageDescriptions.get(count++))
                                                    .build());
                                        }
                                    }
                                } else {
                                    System.out.println("No source element with data-srcset attribute found");
                                }
                            } else {
                                System.out.println("No picture element found");
                            }
                        }
                        if (CollectionUtils.isNotEmpty(images))
                            crawledArticleRepository.update(new org.bson.Document("_id", crawledArticle.getId()), new org.bson.Document("images", images));
                    } catch (Exception ex) {
                        log.warn("Cannot get article url: {}, reason: {}", crawledArticle.getUrl(), ex.getMessage());
                    }
                    urls.add(crawledArticle.getUrl());
                }
                log.info("-----End Page: {} - Size: {}", finalI, crawledArticles.size());
            }, executor);
        }
        return Optional.of(true);
    }

    private CrawledArticle crawl(String url) {
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
            return CrawledArticle.builder()
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
            crawledArticleErrorService.addIfNotExist(CrawledArticleError.builder()
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
        return localDateTime.atZone(zoneId).toEpochSecond();
    }

    private void verifyUrl(List<String> originUrls) {
        originUrls.removeIf(url -> url.contains("https://video."));
        List<CrawledArticle> crawledArticles = crawledArticleService.getByUrls(originUrls);
        if (CollectionUtils.isNotEmpty(crawledArticles)) {
            List<String> existedUrls = crawledArticles.stream().map(CrawledArticle::getUrl).toList();
            originUrls.removeIf(existedUrls::contains);
        }
        List<CrawledArticleError> crawledArticleErrors = crawledArticleErrorService.getByUrls(originUrls);
        if (CollectionUtils.isNotEmpty(crawledArticleErrors)) {
            List<String> existedUrls = crawledArticleErrors.stream().map(CrawledArticleError::getUrl).toList();
            originUrls.removeIf(existedUrls::contains);
        }
    }
}
