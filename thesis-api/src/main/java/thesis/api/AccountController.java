package thesis.api;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import thesis.core.article.Article;
import thesis.core.article.command.CommandCommonQuery;
import thesis.core.article.repository.ArticleRepository;
import thesis.core.article.service.ArticleService;
import thesis.core.news.account.Account;
import thesis.core.news.account.repository.AccountRepository;
import thesis.core.news.command.*;
import thesis.core.news.member.Member;
import thesis.core.news.member.repository.MemberRepository;
import thesis.core.news.role.Role;
import thesis.core.news.role.repository.RoleRepository;
import thesis.core.search_engine.dto.SearchEngineResult;
import thesis.utils.constant.DEFAULT_ROLE;
import thesis.utils.dto.ResponseDTO;
import thesis.utils.helper.PasswordHelper;
import thesis.utils.mail.MailSender;
import thesis.utils.otp.OtpCacheService;

import java.util.*;

@RestController
@RequestMapping("/user")
public class AccountController {
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

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<ResponseDTO<?>> login(@RequestBody CommandLogin command) {
        try {
            Account account = accountRepository.findOne(new Document("email", command.getEmail()), new Document())
                    .orElseThrow(() -> new Exception("Tài khoản không tồn tại"));
            if (!PasswordHelper.checkPassword(command.getPassword(), account.getPassword()))
                throw new Exception("Mật khẩu không đúng");

            Member member = memberRepository.findOne(new Document("_id", new ObjectId(account.getMemberId())), new Document())
                    .orElseThrow(() -> new Exception("Dữ liệu người dùng không tồn tại"));

            Role role = roleRepository.findOne(new Document("_id", new ObjectId(member.getRoleId())), new Document())
                    .orElseThrow(() -> new Exception("Quyền người dùng không tồn tại"));

            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(AccountResponse.builder()
                            .account(account)
                            .member(member)
                            .role(role)
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

    @RequestMapping(method = RequestMethod.POST, value = "/registry")
    public ResponseEntity<ResponseDTO<?>> registry(@RequestBody CommandLogin command) {
        try {
            Optional<Account> existedAccount = accountRepository.findOne(new Document("email", command.getEmail()), new Document());
            if (existedAccount.isPresent())
                throw new Exception("Email đã tồn tại, vui lòng thử email khác");

            Member member = Member.builder()
                    .fullName("Chưa đặt tên")
                    .roleId(DEFAULT_ROLE.MEMBER.getRoleId())
                    .build();
            memberRepository.insert(member);

            Account account = Account.builder()
                    .email(command.getEmail())
                    .password(command.getPassword())
                    .memberId(member.getId().toString())
                    .build();

            Role role = roleRepository.findOne(new Document("_id", new ObjectId(member.getRoleId())), new Document())
                    .orElseThrow(() -> new Exception("Quyền người dùng không tồn tại"));

            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(AccountResponse.builder()
                            .account(account)
                            .member(member)
                            .role(role)
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

    @RequestMapping(method = RequestMethod.POST, value = "/post")
    public ResponseEntity<ResponseDTO<?>> post(@RequestBody CommandPost command) {
        try {
            Member member = memberRepository.findOne(new Document("_id", new ObjectId(command.getMemberId())), new Document())
                    .orElseThrow(() -> new Exception("Tài khoản không tồn tại"));

            Role role = roleRepository.findOne(new Document("_id", new ObjectId(member.getRoleId())), new Document())
                    .orElseThrow(() -> new Exception("Quyền người dùng không tồn tại"));

            if (!role.getNumberValue().equals(DEFAULT_ROLE.AUTHOR.getNumberValue()))
                throw new Exception("Người dùng không có quyền đăng bài");
            if (StringUtils.isBlank(command.getTitle()))
                throw new Exception("Tiêu đề không được để trống");
            if (StringUtils.isBlank(command.getDescription()))
                throw new Exception("Mô tả đề không được để trống");
            if (StringUtils.isBlank(command.getContent()))
                throw new Exception("Nội dung không được để trống");
            if (StringUtils.isBlank(command.getTopic()))
                throw new Exception("Chủ đề không được để trống");

            Article article = Article.builder()
                    .authors(Collections.singletonList(member.getFullName()))
                    .title(command.getTitle())
                    .content(command.getContent())
                    .description(command.getDescription())
                    .topics(Collections.singletonList(command.getTopic()))
                    .labels(CollectionUtils.isNotEmpty(command.getLabels()) ? command.getLabels() : new ArrayList<>())
                    .images(command.getImages())
                    .publicationDate(System.currentTimeMillis() / 1000)
                    .build();
            articleService.add(article);

            if (CollectionUtils.isEmpty(member.getPublishedArticles()))
                member.setPublishedArticles(new ArrayList<>());
            member.getPublishedArticles().add(article.getId().toHexString());

            memberRepository.update(new Document("_id", member.getId()), new Document("publishedArticles", member.getPublishedArticles()));

            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(SearchEngineResult.builder()
                            .articles(Collections.singletonList(article))
                            .build())
                    .message("Đăng bài thành công")
                    .build(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(-1)
                    .message(ex.getMessage())
                    .build(), HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public ResponseEntity<ResponseDTO<?>> save(@RequestBody CommandSave command) {
        try {
            Member member = memberRepository.findOne(new Document("_id", new ObjectId(command.getMemberId())), new Document())
                    .orElseThrow(() -> new Exception("Tài khoản không tồn tại"));

            Article article = articleRepository.findOne(new Document("_id", new ObjectId(command.getArticleId())), new Document())
                    .orElseThrow(() -> new Exception("Bài báo không tồn tại"));

            if (CollectionUtils.isEmpty(member.getSavedArticles()))
                member.setSavedArticles(new ArrayList<>());
            switch (command.getType()) {
                case "save" -> {
                    member.getSavedArticles().add(article.getId().toHexString());
                }
                case "unsave" -> {
                    if(!member.getSavedArticles().remove(article.getId().toHexString()))
                        throw new Exception("Không thể gỡ bài viết hoặc bài viết không tồn tại trong danh sách đã lưu");
                }
            }


            memberRepository.update(new Document("_id", member.getId()), new Document("savedArticles", member.getSavedArticles()));

            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(AccountResponse.builder().member(member).build())
                    .build(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(-1)
                    .message(ex.getMessage())
                    .build(), HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/get_articles")
    public ResponseEntity<ResponseDTO<?>> getArticleByMember(@RequestBody CommandGetArticleByMember command) {
        try {
            List<String> articleIds = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(command.getArticleIds())) {
                articleIds = command.getArticleIds();
            } else {
                Member member = memberRepository.findOne(new Document("_id", new ObjectId(command.getMemberId())), new Document())
                        .orElseThrow(() -> new Exception("Tài khoản không tồn tại"));
                switch (command.getType()) {
                    case "saved" -> articleIds = member.getSavedArticles();
                    case "published" -> articleIds = member.getPublishedArticles();
                    case "viewed" -> articleIds = member.getViewedArticles();
                }
            }

            List<Article> articles = articleService.get(CommandCommonQuery.builder()
                    .articleIds(new HashSet<>(articleIds))
                    .isDescPublicationDate(true)
                    .page(command.getPage())
                    .size(command.getSize())
                    .build());

            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(SearchEngineResult.builder()
                            .articles(articles)
                            .total((long) articles.size())
                            .totalPage((articles.size() + command.getSize() - 1) / command.getSize())
                            .page(command.getPage())
                            .size(command.getSize())
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

    @RequestMapping(method = RequestMethod.POST, value = "/random_articles")
    public ResponseEntity<ResponseDTO<?>> randomArticles(@RequestBody CommandGetArticleByMember command) {
        try {
            long totalArticle = articleService.count(CommandCommonQuery.builder().build()).orElseThrow();
            int totalPage = (int) ((totalArticle + command.getSize() - 1) / command.getSize());

            List<Article> articles = articleService.get(CommandCommonQuery.builder()
                    .page(new Random().nextInt(totalPage - 1))
                    .size(command.getSize())
                    .build());

            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(SearchEngineResult.builder()
                            .articles(articles)
                            .size(command.getSize())
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

    @RequestMapping(method = RequestMethod.POST, value = "/send-otp")
    public ResponseEntity<ResponseDTO<?>> sendOtp(@RequestBody CommandChangePassword command) {
        try {
            if (StringUtils.isBlank(command.getEmail()))
                throw new Exception("Email không hợp lệ");
            if (accountRepository.findOne(new Document("email", command.getEmail()), new Document()).isEmpty())
                throw new Exception("Tài khoản không tồn tại");
            long waitTime = otpCacheService.canResend(command.getEmail());
            if (waitTime > 0) {
                throw new Exception("Vui lòng gửi lại sau " + waitTime + "s");
            }
            String otp = otpCacheService.generateOTP(4);
            try {
                otpCacheService.storeOtp(command.getEmail(), otp);
                mailSender.send(command.getEmail(), "Đổi mật khẩu", otp);
            } catch (Exception ex) {
                throw new Exception("Gửi otp thất bại, vui lòng thử lại");
            }
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(CommandOtpResponse.builder()
                            .email(command.getEmail())
                            .isSuccess(true)
                            .message("Gửi otp thành công")
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

    @RequestMapping(method = RequestMethod.POST, value = "/verify-otp")
    public ResponseEntity<ResponseDTO<?>> verifyOtp(@RequestBody CommandChangePassword command) {
        try {
            if (StringUtils.isBlank(command.getEmail()))
                throw new Exception("Email không hợp lệ");
            if (accountRepository.findOne(new Document("email", command.getEmail()), new Document()).isEmpty())
                throw new Exception("Tài khoản không tồn tại");
            if (StringUtils.isBlank(command.getOtp()))
                throw new Exception("Otp không hợp lệ");
            if (!otpCacheService.validateOtp(command.getEmail(), command.getOtp()))
                throw new Exception("Otp không hợp lệ");
            otpCacheService.clearOtp(command.getEmail());
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(CommandOtpResponse.builder()
                            .email(command.getEmail())
                            .isSuccess(true)
                            .message("Xác thực otp thành công")
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

    @RequestMapping(method = RequestMethod.POST, value = "/reset-password")
    public ResponseEntity<ResponseDTO<?>> resetPassword(@RequestBody CommandChangePassword command) {
        try {
            if (StringUtils.isBlank(command.getEmail()))
                throw new Exception("Email không hợp lệ");
            if (StringUtils.isBlank(command.getPassword()))
                throw new Exception("Mật khẩu không hợp lệ");
            Optional<Account> accountOptional = accountRepository.findOne(new Document("email", command.getEmail()), new Document());
            if (accountOptional.isEmpty())
                throw new Exception("Tài khoản không tồn tại");
            accountRepository.update(new Document("_id", accountOptional.get().getId()), new Document("password", PasswordHelper.hashPassword(command.getPassword())));
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(CommandOtpResponse.builder()
                            .email(command.getEmail())
                            .isSuccess(true)
                            .message("Đổi mật khẩu thành công")
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
