package com.example.bigbrotherbe.domain.member.controller;

import com.example.bigbrotherbe.domain.affiliation.service.AffiliationService;
import com.example.bigbrotherbe.domain.member.dto.request.ChangePasswordRequest;
import com.example.bigbrotherbe.domain.member.dto.request.MemberInfoChangeRequest;
import com.example.bigbrotherbe.domain.member.dto.request.MemberRequest;
import com.example.bigbrotherbe.domain.member.dto.request.SignUpDto;
import com.example.bigbrotherbe.domain.member.dto.response.AffiliationResponse;
import com.example.bigbrotherbe.domain.member.dto.response.MemberInfoResponse;
import com.example.bigbrotherbe.domain.member.dto.response.MemberResponse;
import com.example.bigbrotherbe.domain.member.dto.AffiliationListDto;
import com.example.bigbrotherbe.email.entity.EmailRequest;
import com.example.bigbrotherbe.email.entity.EmailVerificationResult;
import com.example.bigbrotherbe.email.component.MailService;
import com.example.bigbrotherbe.common.exception.response.ApiResponse;
import com.example.bigbrotherbe.auth.jwt.dto.response.JwtToken;
import com.example.bigbrotherbe.domain.member.service.MemberService;

import com.example.bigbrotherbe.auth.jwt.service.JwtTokenService;
import com.example.bigbrotherbe.auth.jwt.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.bigbrotherbe.common.exception.enums.SuccessCode.SUCCESS;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberControllerImpl implements MemberController {

    private final MemberService memberService;
    private final AffiliationService affiliationService;
    private final MailService mailService;
    private final JwtTokenService jwtTokenService;

    public ResponseEntity<ApiResponse<MemberResponse>> signUp(SignUpDto signUpDto) {
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, memberService.userSignUp(signUpDto)));
    }

    public ResponseEntity<ApiResponse<JwtToken>> signIn(MemberRequest memberRequest) {
        String memberEmail = memberRequest.getMemberEmail();
        String password = memberRequest.getMemberPass();
        JwtToken jwtToken = memberService.userSignIN(memberEmail, password);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, jwtToken));
    }


    public ResponseEntity<ApiResponse<AffiliationListDto>> test() {
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, memberService.getMemberAffiliationRoleList()));

    }

    public ResponseEntity<ApiResponse<MemberInfoResponse>> inquireMemberInfo() {
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, memberService.inquireMemberInfo()));
    }

    public ResponseEntity<ApiResponse<EmailVerificationResult>> verificateEmail(String email) {
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, mailService.verificationDuplicateEmail(email)));
    }

    public ResponseEntity<ApiResponse<EmailVerificationResult>> sendMessage(EmailRequest emailRequest) {
        mailService.sendCodeToEmail(emailRequest.getEmail());
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, EmailVerificationResult.builder().authResult(true).build()));
    }


    public ResponseEntity<ApiResponse<EmailVerificationResult>> verificationEmail(String email, String code) {
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, mailService.verifiedCode(email, code)));
    }


    public ResponseEntity<ApiResponse<Void>> changePassword(ChangePasswordRequest changePasswordRequest) {
        memberService.changePassword(changePasswordRequest.email(), changePasswordRequest.password());
        return ResponseEntity.ok(ApiResponse.success(SUCCESS));
    }

    public ResponseEntity<ApiResponse<List<AffiliationResponse>>> getCollegesList() {
        List<AffiliationResponse> collegesList = affiliationService.getColleges();
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, collegesList));
    }

    public ResponseEntity<ApiResponse<List<AffiliationResponse>>> getDepartmentList(String councilName) {
        List<AffiliationResponse> departmentsList = affiliationService.getDepartments(councilName);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, departmentsList));
    }

    public ResponseEntity<ApiResponse<TokenDto>> refreshToken(String refreshToken) {
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, jwtTokenService.refreshAccessToken(refreshToken)));
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> memberDeleteSelf() {
        memberService.deleteSelf();
        return ResponseEntity.ok(ApiResponse.success(SUCCESS));
    }

    @Override
    public ResponseEntity<ApiResponse<MemberInfoResponse>> changeMemberInfo(
            MemberInfoChangeRequest memberInfoChangeRequest) {
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, memberService.changeMemberInfo(memberInfoChangeRequest.userName())));
    }

    @Override
    public ResponseEntity<ApiResponse<List<MemberInfoResponse>>> inquireAllUserInfo() {

        return ResponseEntity.ok(ApiResponse.success(SUCCESS, memberService.inquireAllMemberInfo()));
    }

    @Override
    public ResponseEntity<ApiResponse<MemberInfoResponse>> findUserByEmail(String email) {
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, memberService.getUserByEmail(email)));
    }
}
