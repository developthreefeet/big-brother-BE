package com.example.bigbrotherbe.member.service;

import com.example.bigbrotherbe.global.jwt.JwtToken;
import com.example.bigbrotherbe.member.entity.dto.request.MemberDto;
import com.example.bigbrotherbe.member.entity.dto.request.SignUpDto;

public interface MemberService {
    public JwtToken userSignIN(String username, String password);

    public MemberDto userSignUp(SignUpDto signUpDto);

    MemberDto adminSignUp(SignUpDto signUpDto);
}
