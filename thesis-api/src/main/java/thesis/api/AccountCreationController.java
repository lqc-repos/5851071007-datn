package thesis.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import thesis.core.article.command.CommandCommonQuery;
import thesis.core.article.model.author.repository.AuthorRepository;
import thesis.core.article.repository.ArticleRepository;
import thesis.core.article.service.ArticleService;
import thesis.core.news.account.Account;
import thesis.core.news.account.repository.AccountRepository;
import thesis.core.news.member.Member;
import thesis.core.news.member.repository.MemberRepository;
import thesis.core.news.report.news_report.repository.NewsReportRepository;
import thesis.core.news.role.repository.RoleRepository;
import thesis.utils.dto.ResponseDTO;
import thesis.utils.mail.MailSender;
import thesis.utils.otp.OtpCacheService;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/account_creation")
public class AccountCreationController {
    private static final ZoneOffset ZONE_OFFSET = ZoneOffset.of("+07:00");

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private MailSender mailSender;
    @Autowired
    private OtpCacheService otpCacheService;
    @Autowired
    private NewsReportRepository newsReportRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/random_user")
    public ResponseEntity<ResponseDTO<?>> login() {
        try {
            List<String> articles = articleService.get(CommandCommonQuery.builder()
                            .isDescId(true)
                            .page(1)
                            .size(99999)
                            .build())
                    .stream().map(article -> article.getId().toString())
                    .toList();


            List<Member> members = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                List<String> tempIds = new ArrayList<>(articles);
                Random random = new Random();
                Member member = Member.builder()
                        .fullName(generateRandomVietnameseName())
                        .roleId("6664327f2cad8f06e1b410d1")
                        .build();
                Collections.shuffle(tempIds, new Random());
                member.setSavedArticles(tempIds.subList(0, Math.min(tempIds.size(), random.nextInt(20))));
                Collections.shuffle(tempIds, new Random());
                member.setViewedArticles(tempIds.subList(0, Math.min(tempIds.size(), random.nextInt(20))));
                members.add(member);
            }
            memberRepository.insertMany(members);

            List<Account> accounts = new ArrayList<>();
            for (Member member : members) {
                Account account = Account.builder()
                        .email(generateRandomEmail())
                        .password(generateRandomPassword(10))
                        .memberId(member.getId().toString())
                        .build();
                accounts.add(account);
            }
            accountRepository.insertMany(accounts);

            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data("OK")
                    .build(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(-1)
                    .message(ex.getMessage())
                    .build(), HttpStatus.OK);
        }
    }

    public static String convertFullName(String str) {
        String[] parts = str.split("_");
        StringBuilder result = new StringBuilder();
        for (String part : parts) {
            if (part.length() > 0) {
                result.append(Character.toUpperCase(part.charAt(0)))
                        .append(part.substring(1))
                        .append(" ");
            }
        }
        return result.toString().trim();
    }

    public static String generateRandomEmail() {
        String characters = "abcdefghijklmnopqrstuvwxyz";
        String domains = "gmail.com,yahoo.com,hotmail.com,outlook.com";
        String[] domainArray = domains.split(",");

        Random random = new Random();
        StringBuilder email = new StringBuilder();

        // Tạo phần tên của email
        for (int i = 0; i < 8; i++) {
            email.append(characters.charAt(random.nextInt(characters.length())));
        }

        // Thêm dấu @ và domain
        email.append('@');
        email.append(domainArray[random.nextInt(domainArray.length)]);

        return email.toString();
    }

    public static String generateRandomPassword(int length) {
        if (true)
            return "test123";
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_+=<>?";
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }

        return password.toString();
    }

    public static String generateRandomVietnameseName() {
        String[] ho = {"Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Phan", "Vũ", "Đặng", "Bùi", "Đỗ", "Hồ", "Ngô", "Dương", "Lý"};
        String[] tenDem = {"Văn", "Hữu", "Thị", "Minh", "Xuân", "Quốc", "Thanh", "Thành", "Công", "Mạnh", "Hồng", "Bảo"};
        String[] ten = {"Anh", "Bình", "Chi", "Dung", "Hạnh", "Hùng", "Khanh", "Lan", "Linh", "Mai", "Minh", "Nga", "Ngọc", "Phong", "Quang", "Sơn", "Tâm", "Thảo", "Thành", "Trang", "Trung", "Tuấn", "Việt"};

        Random random = new Random();

        String selectedHo = ho[random.nextInt(ho.length)];
        String selectedTenDem = tenDem[random.nextInt(tenDem.length)];
        String selectedTen = ten[random.nextInt(ten.length)];

        return selectedHo + " " + selectedTenDem + " " + selectedTen;
    }
}
