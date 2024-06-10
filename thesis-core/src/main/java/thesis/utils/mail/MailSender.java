package thesis.utils.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class MailSender {
    @Value("${opt.expire-duration}")
    private int expireDuration;

    @Autowired
    private JavaMailSender javaMailSender;

    public void send(
            String to, String subject, String text) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        // Prepare the HTML content
        int expireDurationInMinute = expireDuration / 60;
        String html = String.format("<!DOCTYPE html>\n" +
                "<html lang=\"vi\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Xác Nhận OTP</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Arial', sans-serif;\n" +
                "            background-color: #f4f4f4;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .email-container {\n" +
                "            max-width: 600px;\n" +
                "            margin: auto;\n" +
                "            background: white;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 10px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        .email-header {\n" +
                "            background-color: #007bff;\n" +
                "            color: white;\n" +
                "            padding: 10px;\n" +
                "            border-top-left-radius: 10px;\n" +
                "            border-top-right-radius: 10px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .email-body {\n" +
                "            padding: 20px;\n" +
                "            text-align: center;\n" +
                "            color: #333;\n" +
                "        }\n" +
                "        .otp-code {\n" +
                "            font-size: 24px;\n" +
                "            margin: 20px 0;\n" +
                "            padding: 10px;\n" +
                "            background-color: #e9ecef;\n" +
                "            display: inline-block;\n" +
                "            border-radius: 5px;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "        .instructions {\n" +
                "            font-size: 16px;\n" +
                "            color: #666;\n" +
                "            margin-top: 20px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"email-container\">\n" +
                "        <div class=\"email-header\">\n" +
                "            Mã Xác Minh OTP\n" +
                "        </div>\n" +
                "        <div class=\"email-body\">\n" +
                "            <p>Vui lòng sử dụng mã OTP sau để hoàn tất xác minh của bạn:</p>\n" +
                "            <div class=\"otp-code\">%s</div>\n" +
                "            <p class=\"instructions\">Mã OTP này chỉ có hiệu lực trong vòng %d phút.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n", text, expireDurationInMinute);
        helper.setFrom("noreply@thesisnews.com");
        helper.setTo(to);
        helper.setText(html, true);
        helper.setSubject(subject);

        javaMailSender.send(message);

    }
}
