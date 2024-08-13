package com.example.bigbrotherbe.global.jwt;

import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.domain.member.entity.role.AffiliationMap;
import com.example.bigbrotherbe.domain.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final MemberRepository memberRepository;

    public Member getLoginMember() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            return memberRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("상수처리 해야함. 없는 멤버"));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("상수처링");
        }
    }
}

