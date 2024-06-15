package thesis.api;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import thesis.core.news.account.Account;
import thesis.core.news.account.repository.AccountRepository;
import thesis.core.news.command.CommandReport;
import thesis.core.news.member.Member;
import thesis.core.news.member.repository.MemberRepository;
import thesis.core.news.report.news_report.NewsReport;
import thesis.core.news.report.news_report.repository.NewsReportRepository;
import thesis.core.news.response.ReportResponse;
import thesis.utils.constant.REPORT_TYPE;
import thesis.utils.dto.ResponseDTO;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
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
    private AccountRepository accountRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping(method = RequestMethod.POST, value = "/script_update")
    public ResponseEntity<ResponseDTO<?>> scriptUpdate(@RequestBody CommandReport command) {
        long currentTime = System.currentTimeMillis() / 1000L;
        List<Member> members = memberRepository.find(new Document(), new Document(), new Document());
        int count = 0;
        for (Member member : members) {
            String email = accountRepository
                    .findOne(new Document("memberId", member.getId().toHexString()), new Document()).map(Account::getEmail)
                    .orElse(null);

            long newCreatedDate = 1642958880 + new Random().nextInt((int) (currentTime - 1642958880));

            Map<String, Object> updateQuery = new HashMap<>();
            updateQuery.put("createdDate", newCreatedDate);
            updateQuery.put("email", email);
            updateQuery.put("isActive", true);

            memberRepository.update(new Document("_id", member.getId()), updateQuery);

            System.out.println("--- " + ++count);
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/get")
    public ResponseEntity<ResponseDTO<?>> get(@RequestBody CommandReport command) {
        try {
            if (command.getFromDate() == null || command.getToDate() == null
                    || command.getToDate() < command.getFromDate())
                throw new Exception("Ngày bắt đầu và ngày kết thúc không hợp lệ");
            {
                ZonedDateTime dateTime1 = Instant.ofEpochSecond(command.getFromDate()).atZone(ZONE_OFFSET);
                ZonedDateTime dateTime2 = Instant.ofEpochSecond(command.getToDate()).atZone(ZONE_OFFSET);
                if (ChronoUnit.DAYS.between(dateTime1, dateTime2) > 30) {
                    throw new Exception("Thống kê chỉ khả dụng trong phạm vi 30 ngày");
                }
            }

            if (StringUtils.isEmpty(command.getReportType()))
                throw new Exception("Vui lòng chọn loại báo cáo");

            long startOfDay = LocalDateTime.ofEpochSecond(command.getFromDate(), 0, ZONE_OFFSET).toLocalDate()
                    .atStartOfDay().toEpochSecond(ZONE_OFFSET);
            long endOfDay = LocalDateTime.ofEpochSecond(command.getToDate(), 0, ZONE_OFFSET).toLocalDate()
                    .atTime(LocalTime.MAX).toEpochSecond(ZONE_OFFSET);
            List<ReportResponse.Report> reports = new ArrayList<>();

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

                reports = countByLabel.entrySet().stream()
                        .map(entry -> ReportResponse.Report.builder().key(entry.getKey()).value(entry.getValue()).build())
                        .toList()
                        .stream()
                        .sorted(Comparator.comparing(ReportResponse.Report::getValue).reversed()).toList();
            } else {
                List<Document> allDates = new ArrayList<>();
                LocalDate startDate = Instant.ofEpochSecond(startOfDay).atZone(ZONE_OFFSET).toLocalDate();
                LocalDate endDate = Instant.ofEpochSecond(endOfDay).atZone(ZONE_OFFSET).toLocalDate();

                while (!startDate.isAfter(endDate)) {
                    allDates.add(new Document("date", startDate.toString()).append("count", 0L));
                    startDate = startDate.plusDays(1);
                }

                AggregateIterable<Document> aggregateResult = null;
                switch (REPORT_TYPE.fromValue(command.getReportType())) {
                    case REGISTRY ->
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
                    case PUBLISH ->
                            aggregateResult = articleRepository.getMongoDBOperation().aggregateSpecial(Arrays.asList(
                                    new Document("$match", new Document("isDeleted", false)
                                            .append("publicationDate", new Document("$gte", startOfDay).append("$lte", endOfDay))),
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

                if (aggregateResult != null) {
                    Map<String, Long> resultMap = new HashMap<>();
                    for (Document doc : aggregateResult) {
                        Date date = doc.getDate("date");
                        Long count = Long.valueOf(doc.getInteger("count"));
                        resultMap.put(dateFormat.format(date), count);
                    }

                    for (Document dateDoc : allDates) {
                        String dateStr = dateDoc.getString("date");
                        Date date = Date.from(LocalDate.parse(dateStr).atStartOfDay(ZONE_OFFSET).toInstant());
                        if (resultMap.containsKey(dateFormat.format(date))) {
                            dateDoc.put("count", resultMap.get(dateFormat.format(date)));
                        }

                        reports.add(ReportResponse.Report.builder()
                                .key(dateFormat.format(date))
                                .value(dateDoc.getLong("count"))
                                .build());
                    }
                }
            }

            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(ReportResponse.builder().reports(reports)
                            .totalValue(reports.stream().mapToLong(ReportResponse.Report::getValue).sum())
                            .build())
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
