package thesis.api;

import jakarta.mail.MessagingException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
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
import thesis.core.article.model.author.Author;
import thesis.core.article.model.author.repository.AuthorRepository;
import thesis.core.article.model.label.custom_label.CustomLabel;
import thesis.core.article.model.label.custom_label.repository.CustomLabelRepository;
import thesis.core.article.model.label.loc_label.LOCLabel;
import thesis.core.article.model.label.loc_label.repository.LOCLabelRepository;
import thesis.core.article.model.label.nlp_label.NLPLabel;
import thesis.core.article.model.label.nlp_label.repository.NLPLabelRepository;
import thesis.core.article.model.label.org_label.ORGLabel;
import thesis.core.article.model.label.org_label.repository.ORGLabelRepository;
import thesis.core.article.model.label.per_label.PERLabel;
import thesis.core.article.model.label.per_label.repository.PERLabelRepository;
import thesis.core.article.model.location.Location;
import thesis.core.article.model.location.repository.LocationRepository;
import thesis.core.article.model.topic.Topic;
import thesis.core.article.model.topic.repository.TopicRepository;
import thesis.core.article.repository.ArticleRepository;
import thesis.core.article.service.ArticleService;
import thesis.core.configuration.service.ThesisConfigurationService;
import thesis.core.label_handler.dto.ArticleIdfInfo;
import thesis.core.label_handler.dto.ArticleWorldInfo;
import thesis.core.label_handler.model.article_label_frequency.ArticleLabelFrequency;
import thesis.core.label_handler.model.article_label_frequency.service.ArticleLabelFrequencyService;
import thesis.core.label_handler.model.total_label_frequency.TotalLabelFrequency;
import thesis.core.label_handler.model.total_label_frequency.command.CommandQueryTotalLabelFrequency;
import thesis.core.label_handler.model.total_label_frequency.service.TotalLabelFrequencyService;
import thesis.core.news.account.Account;
import thesis.core.news.account.repository.AccountRepository;
import thesis.core.news.command.*;
import thesis.core.news.member.Member;
import thesis.core.news.member.repository.MemberRepository;
import thesis.core.news.report.news_report.NewsReport;
import thesis.core.news.report.news_report.repository.NewsReportRepository;
import thesis.core.news.response.AccountResponse;
import thesis.core.news.response.OtpResponse;
import thesis.core.news.response.UserResponse;
import thesis.core.news.role.Role;
import thesis.core.news.role.repository.RoleRepository;
import thesis.core.nlp.dto.AnnotatedWord;
import thesis.core.nlp.service.NLPService;
import thesis.core.search_engine.SearchEngine;
import thesis.core.search_engine.dto.SearchEngineResult;
import thesis.utils.constant.ConfigurationName;
import thesis.utils.constant.DEFAULT_ROLE;
import thesis.utils.constant.MAIL_SENDER_TYPE;
import thesis.utils.constant.REPORT_TYPE;
import thesis.utils.dto.ResponseDTO;
import thesis.utils.helper.PageHelper;
import thesis.utils.helper.PasswordHelper;
import thesis.utils.mail.CommandMail;
import thesis.utils.mail.MailSender;
import thesis.utils.otp.OtpCacheService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@Log4j2
public class AccountController {
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
    //nlp process
    @Autowired
    private NLPService nlpService;
    @Autowired
    private ArticleLabelFrequencyService articleLabelFrequencyService;
    @Autowired
    private TotalLabelFrequencyService totalLabelFrequencyService;
    @Autowired
    private ThesisConfigurationService thesisConfigurationService;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private CustomLabelRepository customLabelRepository;
    @Autowired
    private PERLabelRepository perLabelRepository;
    @Autowired
    private ORGLabelRepository orgLabelRepository;
    @Autowired
    private LOCLabelRepository locLabelRepository;
    @Autowired
    private NLPLabelRepository nlpLabelRepository;
    @Autowired
    private SearchEngine searchEngine;

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<ResponseDTO<?>> login(@RequestBody CommandLogin command) {
        try {
            Account account = accountRepository.findOne(new Document("email", command.getEmail()), new Document())
                    .orElseThrow(() -> new Exception("Tài khoản không tồn tại"));
            if (!PasswordHelper.checkPassword(command.getPassword(), account.getPassword()))
                throw new Exception("Mật khẩu không đúng");

            Member member = memberRepository.findOne(new Document("_id", new ObjectId(account.getMemberId())), new Document())
                    .orElseThrow(() -> new Exception("Dữ liệu người dùng không tồn tại"));

            if (BooleanUtils.isNotTrue(member.getIsActive()))
                throw new Exception("Tài khoản đã bị khóa, vui lòng liên hệ admin");

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
                    .fullName(command.getEmail().split("@")[0])
                    .roleId(DEFAULT_ROLE.MEMBER.getRoleId())
                    .isActive(true)
                    .email(command.getEmail())
                    .build();
            memberRepository.insert(member);

            Account account = Account.builder()
                    .email(command.getEmail())
                    .password(PasswordHelper.hashPassword(command.getPassword()))
                    .memberId(member.getId().toString())
                    .build();

            accountRepository.insert(account);

            Role role = roleRepository.findOne(new Document("_id", new ObjectId(member.getRoleId())), new Document())
                    .orElseThrow(() -> new Exception("Quyền người dùng không tồn tại"));

            CompletableFuture.runAsync(() -> {
                try {
                    mailSender.send(CommandMail.builder()
                            .to(command.getEmail())
                            .subject("Đăng ký thành công")
                            .mailSenderType(MAIL_SENDER_TYPE.REGISTERED)
                            .build());
                } catch (MessagingException e) {
                    log.error("Send email is error, email: {}, msg: {}", command.getEmail(), e.getMessage());
                }
            });

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

            // handle nlp
            CompletableFuture.runAsync(() -> processPostedArticle(article));

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
            Article article = articleRepository.findOne(new Document("_id", new ObjectId(command.getArticleId())), new Document())
                    .orElseThrow(() -> new Exception("Bài báo không tồn tại"));

            if ("view".equals(command.getType())) {
                processReportedLabel(Set.of(article.getId().toHexString()));
            }
            if (StringUtils.isBlank(command.getMemberId()))
                return new ResponseEntity<>(ResponseDTO.builder().build(), HttpStatus.OK);

            Member member = memberRepository.findOne(new Document("_id", new ObjectId(command.getMemberId())), new Document())
                    .orElseThrow(() -> new Exception("Tài khoản không tồn tại"));

            if (CollectionUtils.isEmpty(member.getSavedArticles()))
                member.setSavedArticles(new ArrayList<>());
            if (CollectionUtils.isEmpty(member.getViewedArticles()))
                member.setViewedArticles(new ArrayList<>());
            String message = "";
            switch (command.getType()) {
                case "save" -> {
                    if (!member.getSavedArticles().contains(article.getId().toString())) {
                        member.getSavedArticles().add(article.getId().toHexString());
                        memberRepository.update(new Document("_id", member.getId()), new Document("savedArticles", member.getSavedArticles()));
                    }
                    message = "Đã lưu bài viết";
                }
                case "unsave" -> {
                    if (!member.getSavedArticles().remove(article.getId().toHexString()))
                        throw new Exception("Không thể gỡ bài viết hoặc bài viết không tồn tại trong danh sách đã lưu");
                    memberRepository.update(new Document("_id", member.getId()), new Document("savedArticles", member.getSavedArticles()));
                    message = "Đã bỏ lưu bài viết";
                }
                case "view" -> {
                    if (!member.getSavedArticles().contains(article.getId().toString())) {
                        member.getViewedArticles().add(article.getId().toHexString());
                        memberRepository.update(new Document("_id", member.getId()), new Document("viewedArticles", member.getViewedArticles()));
                    }
                }
            }

            Account account = accountRepository.findOne(new Document("memberId", member.getId().toHexString()), new Document())
                    .orElseThrow(() -> new Exception("Tài khoản không tồn tại"));

            Role role = roleRepository.findOne(new Document("_id", new ObjectId(member.getRoleId())), new Document())
                    .orElseThrow(() -> new Exception("Quyền người dùng không tồn tại"));


            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(AccountResponse.builder()
                            .account(account)
                            .member(member)
                            .role(role)
                            .build())
                    .message(message)
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
            List<Article> articles = CollectionUtils.isNotEmpty(articleIds)
                    ? articleService.get(CommandCommonQuery.builder()
                    .articleIds(new HashSet<>(articleIds))
                    .isDescPublicationDate(true)
                    .page(command.getPage())
                    .size(command.getSize())
                    .build())
                    : new ArrayList<>();

            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(SearchEngineResult.builder()
                            .articles(articles)
                            .total((long) (CollectionUtils.isNotEmpty(articleIds) ? articleIds.size() : 0))
                            .totalPage((articleIds.size() + command.getSize() - 1) / command.getSize())
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
                CompletableFuture.runAsync(() ->
                {
                    try {
                        mailSender.send(CommandMail.builder()
                                .to(command.getEmail())
                                .subject("Yêu cầu đổi mật khẩu")
                                .otp(otp)
                                .mailSenderType(MAIL_SENDER_TYPE.OTP)
                                .build());
                    } catch (MessagingException e) {
                        log.error("Send OTP failed");
                        throw new RuntimeException(e);
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new Exception("Gửi otp thất bại, vui lòng thử lại");
            }
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(OtpResponse.builder()
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
                    .data(OtpResponse.builder()
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
                    .data(OtpResponse.builder()
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

    @RequestMapping(method = RequestMethod.POST, value = "/change-password")
    public ResponseEntity<ResponseDTO<?>> changePassword(@RequestBody CommandChangePassword command) {
        try {
            if (StringUtils.isBlank(command.getEmail()))
                throw new Exception("Email không hợp lệ");
            if (StringUtils.isBlank(command.getPassword()))
                throw new Exception("Mật khẩu không hợp lệ");
            Optional<Account> accountOptional = accountRepository.findOne(new Document("email", command.getEmail()), new Document());
            if (accountOptional.isEmpty())
                throw new Exception("Tài khoản không tồn tại");
            if (!PasswordHelper.checkPassword(command.getOldPassword(), accountOptional.get().getPassword()))
                throw new Exception("Mật khẩu không đúng");
            accountRepository.update(new Document("_id", accountOptional.get().getId()), new Document("password", PasswordHelper.hashPassword(command.getPassword())));

            CompletableFuture.runAsync(() -> {
                try {
                    mailSender.send(CommandMail.builder()
                            .to(command.getEmail())
                            .subject("Cập nhật mật khẩu thành công")
                            .mailSenderType(MAIL_SENDER_TYPE.PASSWORD_CHANGED)
                            .build());
                } catch (MessagingException e) {
                    log.error("Send email is error, email: {}, msg: {}", command.getEmail(), e.getMessage());
                }
            });

            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(OtpResponse.builder()
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

    @RequestMapping(method = RequestMethod.POST, value = "/list")
    public ResponseEntity<ResponseDTO<?>> getUsers(@RequestBody CommandGetListUser command) {
        try {
            Member member = memberRepository.findOne(new Document("_id", new ObjectId(command.getMemberId())), new Document())
                    .orElseThrow(() -> new Exception("Tài khoản không tồn tại"));

            Role role = roleRepository.findOne(new Document("_id", new ObjectId(member.getRoleId())), new Document())
                    .orElseThrow(() -> new Exception("Quyền người dùng không tồn tại"));

            if (!role.getNumberValue().equals(DEFAULT_ROLE.ADMIN.getNumberValue()))
                throw new Exception("Người dùng không có quyền");
            Map<String, Object> queryMember = new HashMap<>();
            queryMember.put("roleId", new Document("$ne", DEFAULT_ROLE.ADMIN.getRoleId()));

            if (StringUtils.isNotBlank(command.getKeyword())) {
                Map<String, Object> textSearch = new HashMap<>();
                Map<String, Object> search = new HashMap<>();
                search.put("$search", command.getKeyword());
                textSearch.put("$text", search);

                Map<String, Object> regex = new HashMap<>();
                regex.put("$regex", Pattern.compile(command.getKeyword(), Pattern.CASE_INSENSITIVE));

                Map<String, Object> queryEmail = new HashMap<>();
                queryEmail.put("email", regex);

                Map<String, Object> queryName = new HashMap<>();
                queryName.put("fullName", regex);

                queryMember.put("$or", Arrays.asList(queryEmail, queryName));
            }

            Long totalUser = memberRepository.count(new Document(queryMember)).orElse(0L);
            List<Member> members = memberRepository.find(queryMember,
                    new Document("createdDate", -1),
                    new Document(),
                    PageHelper.getSkip(command.getPage(), command.getSize()),
                    command.getSize());
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(UserResponse.builder()
                            .keyword(command.getKeyword())
                            .members(members.stream().map(mem -> UserResponse.MemberResponse.builder()
                                    .id(mem.getId().toString())
                                    .fullName(mem.getFullName())
                                    .email(mem.getEmail())
                                    .createdDate(mem.getCreatedDate())
                                    .role(DEFAULT_ROLE.getRoleById(mem.getRoleId()))
                                    .roleValue(DEFAULT_ROLE.getRoleNum(mem.getRoleId()))
                                    .isActive(mem.getIsActive())
                                    .build()).collect(Collectors.toList()))
                            .page(command.getPage())
                            .size(command.getSize())
                            .total(totalUser)
                            .totalPage(PageHelper.getTotalPage(totalUser, command.getSize()))
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

    @RequestMapping(method = RequestMethod.POST, value = "/update")
    public ResponseEntity<ResponseDTO<?>> updateUser(@RequestBody CommandUpdateUser command) {
        try {
            if (StringUtils.isBlank(command.getUpdateMemberId()))
                throw new Exception("Vui lòng thêm người dùng cần cập nhật");
            Map<String, Object> updateQuery = new HashMap<>();

            Member updateMember = memberRepository.findOne(new Document("_id", new ObjectId(command.getUpdateMemberId())), new Document())
                    .orElseThrow(() -> new Exception("Thông tin người dùng không tồn tại"));

            if (command.getRoleLevel() != null || command.getIsActive() != null) {
                Member adminMember = memberRepository.findOne(new Document("_id", new ObjectId(command.getMemberId())), new Document())
                        .orElseThrow(() -> new Exception("Tài khoản không tồn tại"));

                Role role = roleRepository.findOne(new Document("_id", new ObjectId(adminMember.getRoleId())), new Document())
                        .orElseThrow(() -> new Exception("Quyền người dùng không tồn tại"));

                if (!role.getNumberValue().equals(DEFAULT_ROLE.ADMIN.getNumberValue()))
                    throw new Exception("Người dùng không có quyền");

                if (StringUtils.isBlank(command.getType()))
                    throw new Exception("Type không hợp lệ");

                if ("role".equals(command.getType()) && command.getRoleLevel() != null) {
                    updateMember.setRoleId(DEFAULT_ROLE.getRoleIdByNumberValue(command.getRoleLevel()));
                    updateQuery.put("roleId", updateMember.getRoleId());
                }

                if ("status".equals(command.getType()) && command.getIsActive() != null) {
                    updateMember.setIsActive(command.getIsActive());
                    updateQuery.put("isActive", updateMember.getIsActive());
                }
            } else {
                if (!updateMember.getId().toString().equals(command.getMemberId()))
                    throw new Exception("Vui lòng đăng nhập và thử lại");
                if (StringUtils.isNotBlank(command.getFullName())) {
                    updateMember.setFullName(command.getFullName());
                    updateQuery.put("fullName", updateMember.getFullName());
                }
            }

            if (MapUtils.isEmpty(updateQuery))
                throw new Exception("Không có thông tin cần cập nhật, vui lòng kiểm tra lại");

            memberRepository.update(new Document("_id", updateMember.getId()), updateQuery);

            Account account = accountRepository.findOne(new Document("memberId", updateMember.getId().toHexString()), new Document())
                    .orElseThrow(() -> new Exception("Tài khoản không tồn tại"));

            Role role = roleRepository.findOne(new Document("_id", new ObjectId(updateMember.getRoleId())), new Document())
                    .orElseThrow(() -> new Exception("Quyền người dùng không tồn tại"));

            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(AccountResponse.builder()
                            .account(account)
                            .member(updateMember)
                            .role(role)
                            .build())
                    .message("Cập nhật thông tin thành công")
                    .build(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(-1)
                    .message(ex.getMessage())
                    .build(), HttpStatus.OK);
        }
    }

    static int duplicateSave = 0;

    private void processReportedLabel(Set<String> labels) {
        if (duplicateSave % 2 != 0) {
            duplicateSave = 0;
            return;
        }
        duplicateSave++;
        long currentTime = System.currentTimeMillis() / 1000;
        long startOfDay = LocalDateTime.ofEpochSecond(currentTime, 0, ZONE_OFFSET).toLocalDate()
                .atStartOfDay().toEpochSecond(ZONE_OFFSET);

        if (CollectionUtils.isEmpty(labels))
            return;

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("reportDate", startOfDay);
        queryMap.put("reportType", REPORT_TYPE.VIEW.getValue());
        Map<String, Object> sortMap = new HashMap<>();
        sortMap.put("updatedDate", -1);
        sortMap.put("createdDate", -1);

        NewsReport newsReport = newsReportRepository.findOne(queryMap, sortMap).orElse(null);
        if (newsReport == null) {
            newsReport = NewsReport.builder()
                    .reportDate(startOfDay)
                    .reportType(REPORT_TYPE.VIEW.getValue())
                    .labelCounts(new HashMap<>())
                    .build();
            newsReportRepository.insert(newsReport);
        }

        for (String label : labels) {
            newsReport.getLabelCounts().compute(label, (k, v) -> (v == null) ? 1 : v + 1);
        }

        newsReportRepository.update(new Document("_id", newsReport.getId()), new Document("labelCounts", newsReport.getLabelCounts()));
    }

    private void processPostedArticle(Article article) {
        try {
            List<String> NER_TYPES = Arrays.asList("B-PER", "B-ORG", "B-LOC", "I-PER", "I-ORG", "I-LOC");

            Map<String, Long> totalArticleLabels = new HashMap<>();
            String contentBuilder = article.getTitle().trim() + "\n" +
                    article.getDescription().trim() + "\n" +
                    article.getContent().trim();
            AnnotatedWord annotatedWord = nlpService.annotate(contentBuilder).orElseThrow(() -> new Exception("Annotated word is empty"));
            Map<String, ArticleWorldInfo> articleWorldInfoMap = new HashMap<>();
            annotatedWord.getTaggedWords().forEach(taggedWord -> {
                ArticleWorldInfo worldInfo = articleWorldInfoMap.getOrDefault(taggedWord.getWord(),
                        ArticleWorldInfo.builder()
                                .ner(taggedWord.getNer())
                                .count(0L)
                                .build());
                worldInfo.incrementCount();
                articleWorldInfoMap.put(taggedWord.getWord(), worldInfo);
            });
            articleWorldInfoMap.keySet().forEach(key -> {
                if (!totalArticleLabels.containsKey(key))
                    totalArticleLabels.put(key, 1L);
                else
                    totalArticleLabels.put(key, totalArticleLabels.get(key) + 1);
            });
            long totalLabel = articleWorldInfoMap.values().stream().mapToLong(ArticleWorldInfo::getCount).sum();
            List<ArticleLabelFrequency.LabelPerArticle> labelPerArticles = articleWorldInfoMap.entrySet().stream()
                    .map(k -> ArticleLabelFrequency.LabelPerArticle.builder()
                            .label(k.getKey())
                            .count(k.getValue().getCount())
                            .ner(k.getValue().getNer())
                            .build())
                    .sorted(Comparator.comparing(ArticleLabelFrequency.LabelPerArticle::getCount).reversed())
                    .toList();
            ArticleLabelFrequency articleLabelFrequency = ArticleLabelFrequency.builder()
                    .articleId(article.getId().toString())
                    .totalLabel(totalLabel)
                    .labels(labelPerArticles)
                    .build();
            articleLabelFrequencyService.add(articleLabelFrequency);
            totalLabelFrequencyService.increase(totalLabelFrequencyService.getExistedLabel(), totalArticleLabels);

            //todo: author, topic...
            List<String> authorList = article.getAuthors();
            if (CollectionUtils.isNotEmpty(authorList)) {
                for (String authorStr : authorList) {
                    if (StringUtils.isBlank(authorStr))
                        continue;
                    String authorKey = authorStr.toLowerCase().replaceAll(" ", "_");
                    Author author = authorRepository.findOne(new Document("author", authorKey), new Document())
                            .or(() -> {
                                Author authorTemp = Author.builder()
                                        .author(authorKey)
                                        .articleIds(new HashSet<>())
                                        .build();
                                authorRepository.insert(authorTemp);
                                return Optional.ofNullable(authorTemp);
                            }).orElse(null);
                    author.getArticleIds().add(article.getId().toHexString());
                    authorRepository.update(new Document("_id", author.getId()), new Document("articleIds", author.getArticleIds()));
                }
            }
            //handle topic
            List<String> topicList = article.getTopics();
            if (CollectionUtils.isNotEmpty(topicList)) {
                for (String topicStr : topicList) {
                    if (StringUtils.isBlank(topicStr))
                        continue;
                    String topicKey = topicStr.toLowerCase().replaceAll(" ", "_");
                    Topic topic = topicRepository.findOne(new Document("topic", topicKey), new Document())
                            .or(() -> {
                                Topic topicTemp = Topic.builder()
                                        .topic(topicKey)
                                        .articleIds(new HashSet<>())
                                        .build();
                                topicRepository.insert(topicTemp);
                                return Optional.of(topicTemp);
                            }).orElse(null);
                    topic.getArticleIds().add(article.getId().toHexString());
                    topicRepository.update(new Document("_id", topic.getId()), new Document("articleIds", topic.getArticleIds()));
                }
            }
            //handle location
            String locationStr = article.getLocation();
            if (StringUtils.isNotBlank(locationStr)) {
                String locationKey = locationStr.toLowerCase().replaceAll(" ", "_");
                Location location = locationRepository.findOne(new Document("location", locationKey), new Document())
                        .or(() -> {
                            Location locationTemp = Location.builder()
                                    .location(locationKey)
                                    .articleIds(new HashSet<>())
                                    .build();
                            locationRepository.insert(locationTemp);
                            return Optional.of(locationTemp);
                        }).orElse(null);
                location.getArticleIds().add(article.getId().toHexString());
                locationRepository.update(new Document("_id", location.getId()), new Document("articleIds", location.getArticleIds()));
            }
            //handle custom labels
            List<String> customLabelList = article.getLabels();
            if (CollectionUtils.isNotEmpty(customLabelList)) {
                for (String customLabelStr : customLabelList) {
                    if (StringUtils.isBlank(customLabelStr))
                        continue;
                    String customLabelKey = customLabelStr.toLowerCase().replaceAll(" ", "_");
                    CustomLabel customLabel = customLabelRepository.findOne(new Document("label", customLabelKey), new Document())
                            .or(() -> {
                                CustomLabel customLabelTemp = CustomLabel.builder()
                                        .label(customLabelKey)
                                        .articleIds(new HashSet<>())
                                        .build();
                                customLabelRepository.insert(customLabelTemp);
                                return Optional.of(customLabelTemp);
                            }).orElse(null);
                    customLabel.getArticleIds().add(article.getId().toHexString());
                    customLabelRepository.update(new Document("_id", customLabel.getId()), new Document("articleIds", customLabel.getArticleIds()));
                }
            }

            //todo: calculate tf-idf
            if (articleLabelFrequency.getTotalLabel() <= 0 || CollectionUtils.isEmpty(articleLabelFrequency.getLabels())) {
                throw new Exception("Total label equals zero or label list is empty");
            }
            for (ArticleLabelFrequency.LabelPerArticle labelPerArticle : articleLabelFrequency.getLabels()) {
                double tf = BigDecimal.valueOf(labelPerArticle.getCount())
                        .divide(BigDecimal.valueOf(articleLabelFrequency.getTotalLabel()), 20, RoundingMode.CEILING).doubleValue();
                labelPerArticle.setTf(tf);
            }
            articleLabelFrequencyService.updateOne(articleLabelFrequency).orElseThrow();

            //todo: handle org, per,...
            List<TotalLabelFrequency> totalLabelFrequencies = totalLabelFrequencyService.getMany(CommandQueryTotalLabelFrequency.builder()
                    .hasProjection(true)
                    .totalLabelProjection(CommandQueryTotalLabelFrequency.TotalLabelProjection.builder()
                            .isId(false)
                            .isLabel(true)
                            .isCount(true)
                            .build())
                    .build());
            if (CollectionUtils.isEmpty(totalLabelFrequencies))
                throw new Exception("Existed labels is empty");
            Map<String, Long> labelWithCountMap = totalLabelFrequencies.stream()
                    .collect(Collectors.toMap(TotalLabelFrequency::getLabel, TotalLabelFrequency::getCount));

            double eligibleRate = thesisConfigurationService.getByName(ConfigurationName.ELIGIBLE_RATE.getName())
                    .map(m -> Double.valueOf(m.getValue()))
                    .orElse(0.05D);

            long totalArticle = articleLabelFrequencyService.count().orElseThrow();

            ArticleIdfInfo articleIdfInfo = ArticleIdfInfo.builder()
                    .articleId(articleLabelFrequency.getArticleId())
                    .TfIdfInfos(new ArrayList<>())
                    .build();
            for (ArticleLabelFrequency.LabelPerArticle labelPerArticle : articleLabelFrequency.getLabels()) {
                double idf = Math.log(BigDecimal.valueOf(totalArticle)
                        .divide(BigDecimal.valueOf(labelWithCountMap.get(labelPerArticle.getLabel())), 20, RoundingMode.CEILING)
                        .doubleValue());
                double tfIdf = BigDecimal.valueOf(labelPerArticle.getTf())
                        .multiply(BigDecimal.valueOf(idf))
                        .setScale(20, RoundingMode.CEILING).doubleValue();
                if (tfIdf >= eligibleRate || NER_TYPES.contains(labelPerArticle.getNer())) {
                    articleIdfInfo.getTfIdfInfos().add(ArticleIdfInfo.TfIdfInfo.builder()
                            .label(labelPerArticle.getLabel())
                            .ner(labelPerArticle.getNer())
                            .tf(labelPerArticle.getTf())
                            .idf(idf)
                            .tfIdf(tfIdf)
                            .build());
                }
            }
            for (ArticleIdfInfo.TfIdfInfo tfIdfInfo : articleIdfInfo.getTfIdfInfos()) {
                String label = tfIdfInfo.getLabel().toLowerCase().trim();
                switch (tfIdfInfo.getNer()) {
                    case "B-PER", "I-PER" -> {
                        PERLabel perLabel = perLabelRepository.findOne(new Document("label", label), new Document())
                                .or(() -> {
                                    PERLabel perLabelTemp = PERLabel.builder()
                                            .label(label)
                                            .articleIds(new HashSet<>())
                                            .build();
                                    perLabelRepository.insert(perLabelTemp);
                                    return Optional.of(perLabelTemp);
                                }).orElse(null);
                        perLabel.getArticleIds().add(articleIdfInfo.getArticleId());
                        perLabelRepository.update(new Document("_id", perLabel.getId()), new Document("articleIds", perLabel.getArticleIds()));
                    }
                    case "B-ORG", "I-ORG" -> {
                        ORGLabel orgLabel = orgLabelRepository.findOne(new Document("label", label), new Document())
                                .or(() -> {
                                    ORGLabel orgLabelTemp = ORGLabel.builder()
                                            .label(label)
                                            .articleIds(new HashSet<>())
                                            .build();
                                    orgLabelRepository.insert(orgLabelTemp);
                                    return Optional.of(orgLabelTemp);
                                }).orElse(null);
                        orgLabel.getArticleIds().add(articleIdfInfo.getArticleId());
                        orgLabelRepository.update(new Document("_id", orgLabel.getId()), new Document("articleIds", orgLabel.getArticleIds()));
                    }
                    case "B-LOC", "I-LOC" -> {
                        LOCLabel locLabel = locLabelRepository.findOne(new Document("label", label), new Document())
                                .or(() -> {
                                    LOCLabel locLabelTemp = LOCLabel.builder()
                                            .label(label)
                                            .articleIds(new HashSet<>())
                                            .build();
                                    locLabelRepository.insert(locLabelTemp);
                                    return Optional.of(locLabelTemp);
                                }).orElse(null);
                        locLabel.getArticleIds().add(articleIdfInfo.getArticleId());
                        locLabelRepository.update(new Document("_id", locLabel.getId()), new Document("articleIds", locLabel.getArticleIds()));
                    }
                    default -> {
                        NLPLabel nlpLabel = nlpLabelRepository.findOne(new Document("label", label), new Document())
                                .or(() -> {
                                    NLPLabel nlpLabelTemp = NLPLabel.builder()
                                            .label(label)
                                            .articleIds(new HashSet<>())
                                            .build();
                                    nlpLabelRepository.insert(nlpLabelTemp);
                                    return Optional.of(nlpLabelTemp);
                                }).orElse(null);
                        nlpLabel.getArticleIds().add(articleIdfInfo.getArticleId());
                        nlpLabelRepository.update(new Document("_id", nlpLabel.getId()), new Document("articleIds", nlpLabel.getArticleIds()));
                    }
                }
            }
            searchEngine.init();
            log.info("Done annotate new post");
        } catch (Exception ex) {
            log.warn("Cannot annotate posted articleId: {}, reason: {}", article.getId().toString(), ex.getMessage());
        }

    }
}
