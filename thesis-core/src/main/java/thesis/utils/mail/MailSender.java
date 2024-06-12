package thesis.utils.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import thesis.utils.constant.MAIL_SENDER_TYPE;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Log4j2
public class MailSender {
    @Value("${opt.expire-duration}")
    private int expireDuration;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
    @Autowired
    private JavaMailSender javaMailSender;

    public void send(CommandMail command) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        String html = getMailContent(command);
        if (StringUtils.isBlank(html)) {
            log.warn("Mail content is empty");
            return;
        }
        helper.setFrom("noreply@thesisnews.com");
        helper.setSubject(command.getSubject());
        helper.setTo(command.getTo());
        helper.setText(html, true);

        javaMailSender.send(message);

    }

    private String getMailContent(CommandMail command) {
        switch (command.getMailSenderType()) {
            case OTP -> {
                int expireDurationInMinute = expireDuration / 60;
                return String.format(MAIL_SENDER_TYPE.OTP.getTemplate(), command.getOtp(), expireDurationInMinute);
            }
            case PASSWORD_CHANGED -> {
                return String.format(MAIL_SENDER_TYPE.PASSWORD_CHANGED.getTemplate(), simpleDateFormat.format(new Date()));
            }
        }
        return null;
    }
}
