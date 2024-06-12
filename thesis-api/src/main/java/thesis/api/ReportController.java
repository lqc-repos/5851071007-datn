package thesis.api;

import com.mongodb.client.AggregateIterable;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import thesis.core.article.repository.ArticleRepository;
import thesis.core.news.command.CommandReport;
import thesis.core.news.member.repository.MemberRepository;
import thesis.core.news.report.news_report.NewsReport;
import thesis.core.news.report.news_report.repository.NewsReportRepository;
import thesis.core.news.response.ReportResponse;
import thesis.utils.constant.REPORT_TYPE;
import thesis.utils.dto.ResponseDTO;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final ZoneOffset ZONE_OFFSET = ZoneOffset.of("+07:00");

    @Autowired
    private NewsReportRepository newsReportRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @RequestMapping(method = RequestMethod.POST, value = "/get")
    public ResponseEntity<ResponseDTO<?>> get(@RequestBody CommandReport command) {
        try {
            if (command.getFromDate() == null || command.getToDate() == null
                    || command.getToDate() < command.getFromDate())
                throw new Exception("Ngày bắt đầu và ngày kết thúc không hợp lệ");
            if (StringUtils.isEmpty(command.getReportType()))
                throw new Exception("Vui lòng chọn loại báo cáo");

            long startOfDay = LocalDateTime.ofEpochSecond(command.getFromDate(), 0, ZONE_OFFSET).toLocalDate()
                    .atStartOfDay().toEpochSecond(ZONE_OFFSET);
            long endOfDay = LocalDateTime.ofEpochSecond(command.getToDate(), 0, ZONE_OFFSET).toLocalDate()
                    .atTime(LocalTime.MAX).toEpochSecond(ZONE_OFFSET);
            List<ReportResponse> reportResponses = new ArrayList<>();

            if (Arrays.asList(REPORT_TYPE.LABEL.getValue(), REPORT_TYPE.VIEW.getValue()).contains(command.getReportType())) {
                Map<String, Object> query = new HashMap<>();
                query.put("reportType", command.getReportType());
                Map<String, Object> $eq = new HashMap<>();
                $eq.put("$lte", endOfDay);
                $eq.put("$gte", startOfDay);
                query.put("reportDate", $eq);

                List<NewsReport> newsReports = newsReportRepository.find(query, new Document("reportDate", -1), new Document());

                Map<String, Long> countByLabel = new HashMap<>();
                newsReports.forEach(newsReport ->
                        newsReport.getLabelCounts().forEach((key, value) -> countByLabel.merge(key, value, Long::sum))
                );

                reportResponses = countByLabel.entrySet().stream()
                        .map(entry -> ReportResponse.builder().key(entry.getKey()).value(entry.getValue()).build())
                        .toList()
                        .stream()
                        .sorted(Comparator.comparing(ReportResponse::getValue).reversed()).toList();
            } else {
                AggregateIterable<Document> aggregateResult = null;
                switch (REPORT_TYPE.fromValue(command.getReportType())) {
                    case REGISTRY -> {
                        aggregateResult = memberRepository.getMongoDBOperation().aggregateSpecial(Arrays.asList(
                                new Document("$match", new Document("isDeleted", false)
                                        .append("isActive", true)
                                        .append("createdDate", new Document("$gte", startOfDay).append("$lte", endOfDay))),
                                new Document("$group", new Document("_id", new Document("year", new Document("$year", new Document("$toDate", new Document("$multiply", Arrays.asList("$createdDate", 1000)))))
                                        .append("month", new Document("$month", new Document("$toDate", new Document("$multiply", Arrays.asList("$createdDate", 1000)))))
                                        .append("day", new Document("$dayOfMonth", new Document("$toDate", new Document("$multiply", Arrays.asList("$createdDate", 1000))))))
                                        .append("count", new Document("$sum", 1))),
                                new Document("$sort", new Document("_id.year", 1).append("_id.month", 1).append("_id.day", 1)),
                                new Document("$project", new Document("_id", 0)
                                        .append("date", new Document("$dateFromParts", new Document("year", "$_id.year")
                                                .append("month", "$_id.month")
                                                .append("day", "$_id.day")))
                                        .append("count", 1))
                        ));
                    }
                    case PUBLISH -> {
                        aggregateResult = articleRepository.getMongoDBOperation().aggregateSpecial(Arrays.asList(
                                new Document("$match", new Document("isDeleted", false)
                                        .append("createdDate", new Document("$gte", startOfDay).append("$lte", endOfDay))),
                                new Document("$group", new Document("_id", new Document("year", new Document("$year", new Document("$toDate", new Document("$multiply", Arrays.asList("$publicationDate", 1000)))))
                                        .append("month", new Document("$month", new Document("$toDate", new Document("$multiply", Arrays.asList("$publicationDate", 1000)))))
                                        .append("day", new Document("$dayOfMonth", new Document("$toDate", new Document("$multiply", Arrays.asList("$publicationDate", 1000))))))
                                        .append("count", new Document("$sum", 1))),
                                new Document("$sort", new Document("_id.year", 1).append("_id.month", 1).append("_id.day", 1)),
                                new Document("$project", new Document("_id", 0)
                                        .append("date", new Document("$dateFromParts", new Document("year", "$_id.year")
                                                .append("month", "$_id.month")
                                                .append("day", "$_id.day")))
                                        .append("count", 1))
                        ));
                    }
                }
                if (aggregateResult != null)
                    for (Document doc : aggregateResult) {
                        Date date = doc.getDate("date");
                        Long count = Long.valueOf(doc.getInteger("count"));
                        reportResponses.add(ReportResponse.builder()
                                .key(dateFormat.format(date))
                                .value(count)
                                .build());
                    }
            }

            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(reportResponses)
                    .build(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(-1)
                    .message(ex.getMessage())
                    .build(), HttpStatus.OK);
        }
    }
}
