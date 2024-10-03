package com.example.bigbrotherbe.domain.member.service;


import com.example.bigbrotherbe.domain.member.component.MemberManager;
import com.example.bigbrotherbe.domain.member.dto.request.SignUpDto;
import com.example.bigbrotherbe.domain.member.dto.response.MemberInfoResponse;
import com.example.bigbrotherbe.domain.member.dto.response.MemberResponse;
import com.example.bigbrotherbe.domain.member.dto.AffiliationListDto;
import com.example.bigbrotherbe.auth.jwt.entity.JwtToken;
import com.example.bigbrotherbe.domain.member.entity.Member;


import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberManager memberManager;

    @Override
    public MemberResponse userSignUp(SignUpDto signUpDto) {
        return memberManager.userSignup(signUpDto);
    }

    @Override
    public JwtToken userSignIN(String email, String password) {
        return memberManager.signIn(email, password);
    }


    @Override
    public MemberInfoResponse inquireMemberInfo() {
        Member member = memberManager.getLoginMember();
        return memberManager.toMemberInfoResponse(member);
    }


    /*
    거의 모든 도메인 validation으로 사용
     */

    @Override
    public void changePassword(String email, String password) {
        memberManager.changePassword(email,password);
    }

    @Override
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
