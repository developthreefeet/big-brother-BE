package com.example.bigbrotherbe.domain.member.service;

import com.example.bigbrotherbe.domain.member.entity.dto.request.MemberDto;
import com.example.bigbrotherbe.domain.member.entity.dto.request.SignUpDto;
import com.example.bigbrotherbe.global.jwt.JwtToken;

public interface MemberService {
    public JwtToken userSignIN(String username, String password);

    public MemberDto userSignUp(SignUpDto signUpDto);

    MemberDto adminSignUp(SignUpDto signUpDto);
}
