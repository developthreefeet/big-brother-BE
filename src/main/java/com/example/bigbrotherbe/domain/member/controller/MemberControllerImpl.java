package com.example.bigbrotherbe.domain.member.controller;

import com.example.bigbrotherbe.domain.member.dto.request.ChangePasswordRequest;
import com.example.bigbrotherbe.domain.member.dto.request.MemberRequest;
import com.example.bigbrotherbe.domain.member.dto.request.SignUpDto;
import com.example.bigbrotherbe.domain.member.dto.response.MemberInfoResponse;
import com.example.bigbrotherbe.domain.member.dto.response.MemberResponse;
import com.example.bigbrotherbe.domain.member.entity.role.AffiliationMap;
import com.example.bigbrotherbe.global.email.EmailRequest;
import com.example.bigbrotherbe.global.email.EmailVerificationResult;
import com.example.bigbrotherbe.global.exception.response.ApiResponse;
import com.example.bigbrotherbe.global.jwt.AuthUtil;
import com.example.bigbrotherbe.global.jwt.JwtToken;
import com.example.bigbrotherbe.global.jwt.JwtTokenProvider;
import com.example.bigbrotherbe.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static com.example.bigbrotherbe.global.exception.enums.SuccessCode.SUCCESS;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberControllerImpl implements MemberController {

    private final MemberService memberService;
    private final AuthUtil authUtil;
    private final JwtTokenProvider jwtTokenProvider;

    public ResponseEntity<ApiResponse<MemberResponse>> signUp( SignUpDto signUpDto) {
        MemberResponse memberResponse = memberService.userSignUp(signUpDto);

        // ApiResponse 생성
//        ApiResponse<MemberResponse> response = ApiResponse.ok(memberResponse);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, memberResponse));
    }

    public JwtToken signIn(MemberRequest memberRequest) {
        String memberEmail = memberRequest.getMemberEmail();
        String password = memberRequest.getMemberPass();
        // 컨트롤러가 없어도 굴러가게 만들어야 하는 데 그러면 Request 객체를 그대로 넘겨주나?
        JwtToken jwtToken = memberService.userSignIN(memberEmail, password);
        log.info("request memberEmail = {}, password = {}", memberEmail, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(),
                jwtToken.getRefreshToken());
        return jwtToken;
    }


    public ResponseEntity<ApiResponse<AffiliationMap>> test() {
        return ResponseEntity.ok(ApiResponse.success(SUCCESS,memberService.getMemberAffiliationRoleList()));

    }

    public ResponseEntity<ApiResponse<MemberInfoResponse>> inquireMemberInfo() {
        return ResponseEntity.ok(ApiResponse.success(SUCCESS,memberService.inquireMemberInfo()));
    }

    public ResponseEntity<EmailVerificationResult> verificateEmail(String email) {
        return ResponseEntity.ok(memberService.verificateEmail(email));
    }

    public ResponseEntity<EmailVerificationResult> sendMessage(EmailRequest emailRequest) {
        memberService.sendCodeToEmail(emailRequest.getEmail());
        return ResponseEntity.ok(EmailVerificationResult.builder().authResult(true).build());
    }


    public ResponseEntity<EmailVerificationResult> verificationEmail(String email, String code) {
        return ResponseEntity.ok(memberService.verifiedCode(email, code));
    }


    public ResponseEntity<ApiResponse<Void>> changePassword( ChangePasswordRequest changePasswordRequest) {
        memberService.changePasswrd(changePasswordRequest.password());
        return ResponseEntity.ok(ApiResponse.success(SUCCESS));
    }
}
