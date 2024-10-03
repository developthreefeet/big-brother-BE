package com.example.bigbrotherbe.domain.member.component;

import com.example.bigbrotherbe.domain.affiliation.component.AffiliationManager;
import com.example.bigbrotherbe.domain.member.dto.request.SignUpDto;
import com.example.bigbrotherbe.domain.member.dto.response.MemberInfoResponse;
import com.example.bigbrotherbe.domain.member.dto.response.MemberResponse;
import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.domain.member.entity.role.Role;
import com.example.bigbrotherbe.domain.member.entity.role.Affiliation;
import com.example.bigbrotherbe.domain.member.dto.AffiliationListDto;
import com.example.bigbrotherbe.domain.member.entity.AffiliationMember;
import com.example.bigbrotherbe.global.auth.util.AuthUtil;
import com.example.bigbrotherbe.global.auth.jwt.dto.response.JwtToken;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberManager {

    private final AuthUtil authUtil;
    private final PasswordEncoder passwordEncoder;
    private final MemberLoader memberLoader;
    private final MemberChecker memberChecker;
    private final MemberDeleter memberDeleter;
    private final MemberSaver memberSaver;
    private final AffiliationManager affiliationManager;


    @Transactional(rollbackFor = Exception.class)
    public MemberResponse userSignup(SignUpDto signUpDto) {
        memberChecker.checkExistUserEmail(signUpDto.getEmail());
        String encodePassword = passwordEncoder.encode(signUpDto.getPassword());
        Member member = signUpDto.toEntity(encodePassword);
        Member savedMember = memberSaver.saveMember(member);
        AffiliationMember collegeMember = affiliationManager.createAfiiliationMember(savedMember, signUpDto.getCollege(), Role.ROLE_USER);
        AffiliationMember affiliationMember = affiliationManager.createAfiiliationMember(savedMember, signUpDto.getAffiliation(), Role.ROLE_USER);

        return MemberResponse.form(
                savedMember.getId(),
                savedMember.getUsername(),
                savedMember.getEmail(),
                savedMember.getCreateAt(),
                collegeMember.getAffiliation().getName(),
                affiliationMember.getAffiliation().getName()
        );
    }

    @Transactional
    public JwtToken signIn(String email, String password) {
        memberLoader.findByMemberEmail(email);
        return authUtil.createAuthenticationToken(email, password);
    }


    public AffiliationListDto getMemberAffiliationRoleList() {
        Member member = authUtil.getLoginMember();
        return affiliationListToEntity(member.getUsername(), affiliationManager.findAllByMemberId(member.getId()));
    }

    public Member getLoginMember() {
        return authUtil.getLoginMember();
    }

    @Transactional
    public void changePassword(String email, String password) {
        Member member = memberLoader.findByMemberEmail(email);
        member.changePassword(passwordEncoder.encode(password));
    }

    @Transactional
    public void deleteSelf() {
        Member member = authUtil.getLoginMember();
        memberDeleter.deleteMember(member);
    }

    @Transactional
    public MemberInfoResponse changeMemberInfo(String username) {
        Member member = authUtil.getLoginMember();
        member.changeName(username);
        return toMemberInfoResponse(member);
    }

    public List<MemberInfoResponse> inquireAllMemberInfo() {
        return memberLoader.getAllMember().stream()
                .filter(this::isUserRoleOnly)  // 조건에 맞는 멤버만 필터링
                .map(this::toMemberInfoResponse)  // Member를 MemberInfoResponse로 변환
                .collect(Collectors.toList());
    }

    public MemberInfoResponse findUserByEmail(String email) {
        Member member = memberLoader.findByMemberEmail(email);
        return MemberInfoResponse
                .builder()
                .email(member.getEmail())
                .memberName(member.getUsername())
                .createAt(member.getCreateAt())
                .updateAt(member.getUpdateAt())
                .affiliationListDto(getMemberAffiliationRoleList())
                .build();
    }

    public MemberInfoResponse toMemberInfoResponse(Member member) {
        return MemberInfoResponse.builder()
                .email(member.getEmail())
                .memberName(member.getUsername())
                .createAt(member.getCreateAt())
                .updateAt(member.getUpdateAt())
                .affiliationListDto(getMemberAffiliationRoleList())
                .build();
    }

    private AffiliationListDto affiliationListToEntity(String userName, List<AffiliationMember> affiliationMemberList) {
        AffiliationListDto affiliationListDto = new AffiliationListDto(userName);

        for (AffiliationMember affiliationMember : affiliationMemberList) {
            Affiliation affiliation = affiliationMember.getAffiliation();
            affiliationListDto.addAffiliation(affiliation.getCouncilType(), affiliation.getName(), affiliationMember.getRole());
        }
        return affiliationListDto;
    }

    private boolean isUserRoleOnly(Member member) {
        return member.getAffiliations().stream()
                .allMatch(affiliationMember -> "ROLE_USER".equals(affiliationMember.getRole()));
    }

}
