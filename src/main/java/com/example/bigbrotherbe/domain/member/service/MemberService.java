package com.example.bigbrotherbe.domain.member.service;

import com.example.bigbrotherbe.domain.member.entity.dto.request.SignUpDto;
import com.example.bigbrotherbe.domain.member.entity.dto.response.MemberResponse;
import com.example.bigbrotherbe.global.email.EmailVerificationResult;
import com.example.bigbrotherbe.global.jwt.JwtToken;

public interface MemberService {
    public JwtToken userSignIN(String username, String password);

    public MemberResponse userSignUp(SignUpDto signUpDto);

    public MemberResponse inquireMemberInfo(String memberName);

    void sendCodeToEmail(String email);

    EmailVerificationResult verifiedCode(String email, String authCode);
}
