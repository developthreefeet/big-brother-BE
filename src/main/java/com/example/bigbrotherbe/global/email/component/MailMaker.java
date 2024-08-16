package com.example.bigbrotherbe.global.email.component;

import com.example.bigbrotherbe.global.email.entity.Email;
import com.example.bigbrotherbe.global.exception.BusinessException;
import com.example.bigbrotherbe.global.exception.enums.ErrorCode;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class MailMaker {
    private static final String  EMAIL_TITLE = "명지대 big brother 이메일 인증 번호";
    public Email makeMail(String toEmail) {
        return Email
            .builder()
            .title(EMAIL_TITLE)
            .authCode(createCode())
            .toEmailAddress(toEmail)
            .build();
    }

    private String createCode() {
        int lenth = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessException(ErrorCode.NO_SUCH_ALGORITHM);
        }
    }
    public SimpleMailMessage createEmailForm(Email email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email.toEmailAddress());
        message.setSubject(email.title());
        message.setText(email.authCode());
        return message;
    }
}
