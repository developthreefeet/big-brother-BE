package com.example.bigbrotherbe.domain.member.component;

import com.example.bigbrotherbe.domain.member.repository.MemberRepository;
import com.example.bigbrotherbe.global.common.exception.BusinessException;
import com.example.bigbrotherbe.global.common.exception.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberChecker {

    private final MemberRepository memberRepository;

    public void checkExistUserEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new BusinessException(ErrorCode.EXIST_EMAIL);
        }
    }

}
