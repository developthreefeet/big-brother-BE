package com.example.bigbrotherbe.global.jwt;

import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

    @Component
    @RequiredArgsConstructor
    public class AuthUtil {

        private final MemberRepository memberRepository;

        public Member getLoginMember() {
            try {
                String loginMember = SecurityContextHolder.getContext().getAuthentication().getName();
                return memberRepository.findByUsername(loginMember)
                    .orElseThrow(() -> new IllegalArgumentException("상수처리 해야함. 없는 멤버"));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("상수처링");
            }
        }
    }

