package com.example.bigbrotherbe.global.email;

import com.example.bigbrotherbe.domain.member.entity.EMailVerification;
import com.example.bigbrotherbe.domain.member.repository.MailRepository;
import com.example.bigbrotherbe.global.exception.BusinessException;
import com.example.bigbrotherbe.global.exception.enums.ErrorCode;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final MailRepository mailRepository;

    //    @Value("${spring.mail.auth-code-expiration-millis}")
    private final long authCodeExpirationMillis = 1800000;
    public void sendCodeToEmail(String toEmail) {
            checkPresentEmail(toEmail);
            String title = "명지대 big brother 이메일 인증 번호";
            String authCode = createCode();
            sendEmail(toEmail, title, authCode);
            // 이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = "AuthCode " + Email / value = AuthCode )
            saveEmailAuthCode(toEmail, authCode, Duration.ofMillis(this.authCodeExpirationMillis));
    }

    private void sendEmail(String toEmail, String title, String text) {
        SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);
        try {
            javaMailSender.send(emailForm);
        } catch (RuntimeException e) {
            log.debug("MailService.sendEmail exception occur toEmail: {}, " +
                "title: {}, text: {}", toEmail, title, text);
            throw new BusinessException(ErrorCode.UNABLE_TO_SEND_EMAIL);
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
    @Transactional
    protected void saveEmailAuthCode(String emailAddress, String authCode, Duration duration) {
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

    @Transactional
    protected void checkPresentEmail(String toEmail) {
        mailRepository.findByEmailAddress(toEmail)
            .ifPresent(mail -> mailRepository.deleteByEmailAddress(toEmail));
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
            log.debug("MemberService.createCode() exception occur");
            throw new BusinessException(ErrorCode.NO_SUCH_ALGORITHM);
        }
    }

    public EmailVerificationResult verifiedCode(String email, String authCode) {
        String redisAuthCode = getAuthCode(email);
        boolean authResult = redisAuthCode.equals(authCode);
        if (!authResult) {
            throw new BusinessException(ErrorCode.MISMATCH_VERIFIED_CODE);
        }
        return EmailVerificationResult.of(true);
    }

}
