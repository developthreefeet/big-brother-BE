package com.example.bigbrotherbe.global.email.component;


import com.example.bigbrotherbe.domain.member.component.MemberLoader;
import com.example.bigbrotherbe.global.email.dto.EmailVerificationResult;
import com.example.bigbrotherbe.global.common.exception.BusinessException;
import com.example.bigbrotherbe.global.common.exception.enums.ErrorCode;
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
