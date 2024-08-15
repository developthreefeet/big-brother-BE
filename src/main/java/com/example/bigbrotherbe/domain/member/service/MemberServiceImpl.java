package com.example.bigbrotherbe.domain.member.service;


import com.example.bigbrotherbe.domain.affiliation.component.AffiliationManger;
import com.example.bigbrotherbe.domain.member.component.MemberManager;
import com.example.bigbrotherbe.domain.member.dto.request.SignUpDto;
import com.example.bigbrotherbe.domain.member.dto.response.AffiliationResponse;
import com.example.bigbrotherbe.domain.member.dto.response.MemberInfoResponse;
import com.example.bigbrotherbe.domain.member.dto.response.MemberResponse;
import com.example.bigbrotherbe.domain.member.entity.enums.AffiliationCode;
import com.example.bigbrotherbe.domain.member.entity.role.Affiliation;
import com.example.bigbrotherbe.domain.member.entity.role.AffiliationListDto;
import com.example.bigbrotherbe.domain.member.entity.role.AffiliationMember;
import com.example.bigbrotherbe.domain.member.component.MemberChecker;
import com.example.bigbrotherbe.domain.member.component.MemberDeleter;
import com.example.bigbrotherbe.domain.member.component.MemberLoader;
import com.example.bigbrotherbe.global.email.EmailVerificationResult;
import com.example.bigbrotherbe.global.email.MailService;
import com.example.bigbrotherbe.global.exception.BusinessException;
import com.example.bigbrotherbe.global.exception.enums.ErrorCode;
import com.example.bigbrotherbe.global.jwt.AuthUtil;
import com.example.bigbrotherbe.global.jwt.JwtToken;
import com.example.bigbrotherbe.domain.member.entity.Member;


import com.example.bigbrotherbe.global.jwt.JwtTokenProvider;
import com.example.bigbrotherbe.global.jwt.entity.TokenDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MailService mailService;
    private final MemberManager memberManager;
    private final AffiliationManger affiliationManger;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MemberResponse userSignUp(SignUpDto signUpDto) {
        return memberManager.userSignup(signUpDto);
    }

    @Transactional
    @Override
    public JwtToken userSignIN(String email, String password) {
        return memberManager.signIn(email, password);
    }


    @Override
    public MemberInfoResponse inquireMemberInfo() {
        Member member = memberManager.getLoginMember();
        return MemberInfoResponse
            .builder()
            .email(member.getEmail())
            .memberName(member.getUsername())
            .createAt(member.getCreateAt())
            .updateAt(member.getUpdateAt())
            .affiliationListDto(getMemberAffiliationRoleList())
            .build();
    }


    // 인증코드 요청 및 일시 저장
    @Override
    @Transactional
    public void sendCodeToEmail(String toEmail) {
        mailService.sendCodeToEmail(toEmail);
    }

    public EmailVerificationResult verifiedCode(String email, String authCode) {
        return mailService.verifiedCode(email,authCode);
    }

    // 이메일 중복 체크
    @Override
    public EmailVerificationResult verificationDuplicateEmail(String email) {
        return memberManager.checkDuplicateEmail(email);
    }

    /*
    거의 모든 도메인 validation으로 사용
     */
    @Override
    @Transactional(readOnly = true)
    public boolean checkExistAffiliationById(Long affiliationId) {
        return affiliationManger.existsById(affiliationId);
    }

    @Override
    @Transactional
    public void changePassword(String email, String password) {
        memberManager.changePassword(email,password);
    }

    @Override
    @Transactional
    public void makeAffiliation() {
//        affiliationRepository.save(Affiliation.builder().affiliation_id(1L).name("총학").build());
    }

    @Override
    public List<AffiliationResponse> getColleges() {
        return Arrays.stream(AffiliationCode.values())
                .filter(code -> code.getCouncilType().equals("단과대"))
                .map(code -> AffiliationResponse.fromAffiliationResponse(code.getVal(), code.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<AffiliationResponse> getDepartments(String councilName) {
        return AffiliationCode.getDepartmentsByCollegeName(councilName);
    }


    @Override
    public TokenDto refreshToken(String refreshToken) {
        return jwtTokenProvider.refreshToken(refreshToken);
    }

    @Override
    @Transactional()
    public void deleteSelf() {
        memberManager.deleteSelf();
    }

    @Override
    @Transactional
    public MemberInfoResponse changeMemberInfo(String username) {
        return memberManager.changeMemberInfo(username);
    }

    @Override
    public List<MemberInfoResponse> inquireAllMemberInfo() {
        return memberManager.inquireAllMemberInfo();
    }

    @Override
    public MemberInfoResponse getUserByEmail(String email) {
        return memberManager.findUserByEmail(email);
    }

    @Override
    public AffiliationListDto getMemberAffiliationRoleList() {
        return memberManager.getMemberAffiliationRoleList();
    }
}
