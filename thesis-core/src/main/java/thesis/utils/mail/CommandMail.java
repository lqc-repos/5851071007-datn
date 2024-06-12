package thesis.utils.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import thesis.utils.constant.MAIL_SENDER_TYPE;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommandMail {
    private String to;
    private String subject;
    private String otp;
    private MAIL_SENDER_TYPE mailSenderType;
}
