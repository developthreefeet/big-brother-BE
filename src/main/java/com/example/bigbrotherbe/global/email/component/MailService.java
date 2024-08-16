package com.example.bigbrotherbe.global.email.component;

import com.example.bigbrotherbe.domain.member.entity.EMailVerification;
import com.example.bigbrotherbe.domain.member.repository.MailRepository;
import com.example.bigbrotherbe.global.email.entity.Email;
import com.example.bigbrotherbe.global.email.entity.EmailVerificationResult;
import com.example.bigbrotherbe.global.exception.BusinessException;
import com.example.bigbrotherbe.global.exception.enums.ErrorCode;
import java.time.Duration;
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
    private final MailChecker mailChecker;
    private final MailMaker mailMaker;
    //    @Value("${spring.mail.auth-code-expiration-millis}")
    private final long authCodeExpirationMillis = 1800000;

    @Transactional
    public void sendCodeToEmail(String toEmail) {
        deleteByEmailAddress(toEmail);
        Email email =  mailMaker.makeMail(toEmail);
        SimpleMailMessage emailForm = mailMaker.createEmailForm(email);
        sendEmail(emailForm);
        saveEmailAuthCode(toEmail, email.authCode(), Duration.ofMillis(this.authCodeExpirationMillis));
    }

    private void sendEmail(SimpleMailMessage emailForm) {
        try {
            javaMailSender.send(emailForm);
        } catch (RuntimeException e) {
            throw new BusinessException(ErrorCode.UNABLE_TO_SEND_EMAIL);
        }
    }


    @Transactional
    protected void saveEmailAuthCode(String emailAddress, String authCode, Duration duration) {
        mailRepository.save(
            EMailVerification.builder().emailAddress(emailAddress).verificationCode(authCode)
                .build());
        // 5분뒤에 삭제.
        scheduleDeletion(emailAddress,duration);
    }

    @Transactional
    public void scheduleDeletion(String emailAddress, Duration duration) {
        new Thread(() -> {
            try {
                Thread.sleep(duration.toMillis());
                mailRepository.deleteByEmailAddress(emailAddress);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    @Transactional(readOnly = true)
    public String getAuthCode(String emailAddress) {
       EMailVerification emailVerification =  mailRepository.findByEmailAddress(emailAddress).orElseThrow
           ( ()-> new IllegalArgumentException("이메일 인증 시간이 만료되었습니다."));
        return emailVerification.getVerificationCode();
    }

    @Transactional
    protected void deleteByEmailAddress(String toEmail) {
        mailRepository.findByEmailAddress(toEmail)
            .ifPresent(mail -> mailRepository.deleteByEmailAddress(toEmail));
    }



    public EmailVerificationResult verifiedCode(String email, String authCode) {
        String saveAuthCode = getAuthCode(email);
        boolean authResult = authCode.equals(saveAuthCode);
        if (!authResult) {
            throw new BusinessException(ErrorCode.MISMATCH_VERIFIED_CODE);
        }
        return EmailVerificationResult.of(true);
    }
    public EmailVerificationResult verificationDuplicateEmail(String email) {
            return mailChecker.checkDuplicateEmail(email);
    }

}
