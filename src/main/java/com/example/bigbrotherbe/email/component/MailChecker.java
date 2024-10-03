package com.example.bigbrotherbe.email.component;


import com.example.bigbrotherbe.domain.member.component.MemberLoader;
import com.example.bigbrotherbe.email.entity.EmailVerificationResult;
import com.example.bigbrotherbe.common.exception.BusinessException;
import com.example.bigbrotherbe.common.exception.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailChecker {
    private final MemberLoader memberLoader;
    public EmailVerificationResult checkDuplicateEmail(String email) {
        if (memberLoader.findByMemberEmailForCheck(email).isPresent()) {
            throw new BusinessException(ErrorCode.EXIST_EMAIL);
        }
        return EmailVerificationResult.builder().authResult(false).build();
    }
}
