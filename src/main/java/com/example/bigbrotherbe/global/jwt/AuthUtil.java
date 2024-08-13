package com.example.bigbrotherbe.global.jwt;

import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.domain.member.entity.role.AffiliationMap;
import com.example.bigbrotherbe.domain.member.entity.role.AffiliationMember;
import com.example.bigbrotherbe.domain.member.repository.AffiliationMemberRepository;
import com.example.bigbrotherbe.domain.member.repository.AffiliationRepository;
import com.example.bigbrotherbe.domain.member.repository.MemberRepository;

import java.util.List;

import com.example.bigbrotherbe.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.example.bigbrotherbe.domain.member.entity.enums.Role.ROLE_PRESIDENT;
import static com.example.bigbrotherbe.domain.member.entity.enums.Role.ROLE_USER;
import static com.example.bigbrotherbe.global.exception.enums.ErrorCode.FAIL_LOAD_MEMBER;
import static com.example.bigbrotherbe.global.exception.enums.ErrorCode.NO_EXIST_MEMBER;

@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final MemberRepository memberRepository;
    private final AffiliationMemberRepository affiliationMemberRepository;

    public Member getLoginMember() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            return memberRepository.findByEmail(email)
                    .orElseThrow(() -> new BusinessException(NO_EXIST_MEMBER));
        } catch (IllegalArgumentException e) {
            throw new BusinessException(FAIL_LOAD_MEMBER);
        }
    }

    public boolean checkCouncilRole(Long affiliationId) {
        Member member = getLoginMember();
        List<AffiliationMember> affiliationMemberList = affiliationMemberRepository.findAllByMemberId(member.getId());

        for (AffiliationMember affiliationMember : affiliationMemberList) {
            if (affiliationMember.getAffiliation().getAffiliation_id().equals(affiliationId) &&
                    affiliationMember.getRole().equals(ROLE_USER.getRole())) {
                return true;
            }
        }
        return false;
    }

    public boolean checkPresidentRole(Long affiliationId) {
        Member member = getLoginMember();
        List<AffiliationMember> affiliationMemberList = affiliationMemberRepository.findAllByMemberId(member.getId());

        for (AffiliationMember affiliationMember : affiliationMemberList) {
            if (affiliationMember.getAffiliation().getAffiliation_id().equals(affiliationId) &&
                    affiliationMember.getRole().equals(ROLE_PRESIDENT.getRole())) {
                return false;
            }
        }
        return true;
    }
}

