package com.example.bigbrotherbe.domain.member.service;

import com.example.bigbrotherbe.domain.member.dto.request.SignUpDto;
import com.example.bigbrotherbe.domain.member.dto.response.MemberInfoResponse;
import com.example.bigbrotherbe.domain.member.dto.response.MemberResponse;
import com.example.bigbrotherbe.domain.member.dto.AffiliationListDto;
import com.example.bigbrotherbe.global.jwt.entity.JwtToken;

import java.util.List;

public interface MemberService {
    JwtToken userSignIN(String username, String password);

    MemberResponse userSignUp(SignUpDto signUpDto);

    MemberInfoResponse inquireMemberInfo();

    void changePassword(String email,String password);

    AffiliationListDto getMemberAffiliationRoleList();

    void deleteSelf();

    MemberInfoResponse changeMemberInfo(String userName);

    List<MemberInfoResponse> inquireAllMemberInfo();

    MemberInfoResponse getUserByEmail(String email);
}
