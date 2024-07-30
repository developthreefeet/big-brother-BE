package com.example.bigbrotherbe.global.email;

import com.example.bigbrotherbe.domain.member.entity.EMailVerification;
import com.example.bigbrotherbe.domain.member.repository.MailRepository;
import com.example.bigbrotherbe.global.exception.BusinessLogicException;
import com.example.bigbrotherbe.global.exception.ExceptionCode;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final MailRepository mailRepository;

    public void sendEmail(String toEmail, String title, String text) {
        SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);
        try {
            javaMailSender.send(emailForm);
        } catch (RuntimeException e) {
            log.debug("MailService.sendEmail exception occur toEmail: {}, " +
                "title: {}, text: {}", toEmail, title, text);
            throw new BusinessLogicException(ExceptionCode.UNABLE_TO_SEND_EMAIL);
        }
    }

    private SimpleMailMessage createEmailForm(String toEmail,
        String title,
        String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }
    @Transactional(readOnly = false)
    public void saveEmailAuthCode(String emailAddress, String authCode, Duration duration) {
        mailRepository.save(
            EMailVerification.builder().emailAddress(emailAddress).verificationCode(authCode)
                .build());
        // 5분뒤에 삭제.
        scheduleDeletion(emailAddress,duration);
    }

    private void scheduleDeletion(String emailAddress, Duration duration) {
        new Thread(() -> {
            try {
                Thread.sleep(duration.toMillis());
                mailRepository.deleteByEmailAddress(emailAddress);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public String getAuthCode(String emailAddress) {
       EMailVerification emailVerification =  mailRepository.findByEmailAddress(emailAddress).orElseThrow
           ( ()-> new IllegalArgumentException("이메일 인증 시간이 만료되었습니다."));
        return emailVerification.getVerificationCode();
    }
}
