package com.example.bigbrotherbe.domain.member.component;

import com.example.bigbrotherbe.domain.affiliation.component.AffiliationManger;
import com.example.bigbrotherbe.domain.member.dto.request.SignUpDto;
import com.example.bigbrotherbe.domain.member.dto.response.MemberInfoResponse;
import com.example.bigbrotherbe.domain.member.dto.response.MemberResponse;
import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.domain.member.entity.enums.Role;
import com.example.bigbrotherbe.domain.member.entity.role.Affiliation;
import com.example.bigbrotherbe.domain.member.entity.role.AffiliationListDto;
import com.example.bigbrotherbe.domain.member.entity.role.AffiliationMember;
import com.example.bigbrotherbe.global.email.EmailVerificationResult;
import com.example.bigbrotherbe.global.exception.BusinessException;
import com.example.bigbrotherbe.global.exception.enums.ErrorCode;
import com.example.bigbrotherbe.global.jwt.AuthUtil;
import com.example.bigbrotherbe.global.jwt.JwtToken;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberManager {

    private final AuthUtil authUtil;
    private final PasswordEncoder passwordEncoder;
    private final MemberLoader memberLoader;
    private final MemberChecker memberChecker;
    private final MemberDeleter memberDeleter;
    private final AffiliationManger affiliationManger;

    public MemberResponse userSignup(SignUpDto signUpDto) {
        memberChecker.checkExistUserEmail(signUpDto.getEmail());

        Member savedMember = memberLoader.signUp(signUpDto,passwordEncoder.encode(signUpDto.getPassword()));
        AffiliationMember collegeMember = affiliationManger.createAfiiliationMember(savedMember,signUpDto.getCollege(), Role.ROLE_USER);
        AffiliationMember affiliationMember = affiliationManger.createAfiiliationMember(savedMember,signUpDto.getAffiliation(),Role.ROLE_USER);
        return MemberResponse.form(
            savedMember.getId(),
            savedMember.getUsername(),
            savedMember.getEmail(),
            savedMember.getCreateAt(),
            collegeMember.getAffiliation().getName(),
            affiliationMember.getAffiliation().getName()
        );
    }

    public JwtToken signIn(String email, String password) {
        memberLoader.findByMemberEmail(email);
        return authUtil.createAuthenticationToken(email,password);
    }


    public AffiliationListDto getMemberAffiliationRoleList() {
        Member member = authUtil.getLoginMember();
        return affiliationListToEntity(member.getUsername(), affiliationManger.findAllByMemberId(member.getId()));
    }

    public Member getLoginMember() {
        return authUtil.getLoginMember();
    }


    public EmailVerificationResult checkDuplicateEmail(String email) {
        if (memberLoader.findByMemberEmailForCheck(email).isPresent()) {
            throw new BusinessException(ErrorCode.EXIST_EMAIL);
        }
        return EmailVerificationResult.builder().authResult(false).build();
    }

    public void changePassword(String email, String password) {
        Member member = memberLoader.findByMemberEmail(email);
        member.changePassword(passwordEncoder.encode(password));
    }

    public void deleteSelf() {
        Member member = authUtil.getLoginMember();
        memberDeleter.deleteMember(member);
    }

    public MemberInfoResponse changeMemberInfo(String username) {
        Member member = authUtil.getLoginMember();
        member.changeName(username);
        return MemberInfoResponse
            .builder()
            .email(member.getEmail())
            .memberName(member.getUsername())
            .createAt(member.getCreateAt())
            .updateAt(member.getUpdateAt())
            .affiliationListDto(getMemberAffiliationRoleList())
            .build();
    }

    public List<MemberInfoResponse> inquireAllMemberInfo() {
        List<MemberInfoResponse> memberInfoResponseList = new ArrayList<>();
        for(Member member: memberLoader.getAllMember()){
            if(distinguishUser(member.getAffiliations())){
                memberInfoResponseList.add(MemberInfoResponse
                    .builder()
                    .email(member.getEmail())
                    .memberName(member.getUsername())
                    .createAt(member.getCreateAt())
                    .updateAt(member.getUpdateAt())
                    .affiliationListDto(getMemberAffiliationRoleList())
                    .build());
            }
        }
        return memberInfoResponseList;
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

    private AffiliationListDto affiliationListToEntity(String userName, List<AffiliationMember> affiliationMemberList) {
        AffiliationListDto affiliationListDto = new AffiliationListDto(userName);

        for (AffiliationMember affiliationMember : affiliationMemberList) {
            Affiliation affiliation = affiliationMember.getAffiliation();
            affiliationListDto.addAffiliation(affiliation.getCouncilType(), affiliation.getName(), affiliationMember.getRole());
        }
        return affiliationListDto;
    }
    private boolean distinguishUser(List<AffiliationMember> affiliations) {
        for(AffiliationMember affiliationMember : affiliations){
            if(!"ROLE_USER".equals(affiliationMember.getRole())){
                return false;
            }
        }
        return true;
    }
}
