package com.example.bigbrotherbe.domain.member.service;

import com.example.bigbrotherbe.domain.member.dto.request.SignUpDto;
import com.example.bigbrotherbe.domain.member.dto.response.AffiliationCollegeResponse;
import com.example.bigbrotherbe.domain.member.dto.response.MemberInfoResponse;
import com.example.bigbrotherbe.domain.member.dto.response.MemberResponse;
import com.example.bigbrotherbe.domain.member.entity.role.AffiliationListDto;
import com.example.bigbrotherbe.global.email.EmailVerificationResult;
import com.example.bigbrotherbe.global.jwt.JwtToken;

import com.example.bigbrotherbe.global.jwt.entity.TokenDto;
import java.util.List;

public interface MemberService {
    JwtToken userSignIN(String username, String password);

    MemberResponse userSignUp(SignUpDto signUpDto);

    MemberInfoResponse inquireMemberInfo();

    void sendCodeToEmail(String email);

    EmailVerificationResult verifiedCode(String email, String authCode);

    EmailVerificationResult verificateEmail(String email);

    boolean checkExistAffiliationById(Long affiliationId);

    void changePasswrd(String email,String password);

    void makeAffiliation();

    AffiliationListDto getMemberAffiliationRoleList();

    List<AffiliationCollegeResponse> getColleges();

    TokenDto refreshToken(String refreshToken);

    void deleteSelf();

    MemberInfoResponse changeMemberInfo(String userName);
}
