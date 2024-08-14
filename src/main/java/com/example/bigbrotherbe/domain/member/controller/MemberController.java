package com.example.bigbrotherbe.domain.member.controller;

import com.example.bigbrotherbe.domain.member.dto.request.ChangePasswordRequest;
import com.example.bigbrotherbe.domain.member.dto.request.MemberInfoChangeRequest;
import com.example.bigbrotherbe.domain.member.dto.request.MemberRequest;
import com.example.bigbrotherbe.domain.member.dto.request.SignUpDto;
import com.example.bigbrotherbe.domain.member.dto.response.AffiliationResponse;
import com.example.bigbrotherbe.domain.member.dto.response.MemberInfoResponse;
import com.example.bigbrotherbe.domain.member.dto.response.MemberResponse;
import com.example.bigbrotherbe.domain.member.entity.role.AffiliationListDto;
import com.example.bigbrotherbe.global.email.EmailRequest;
import com.example.bigbrotherbe.global.email.EmailVerificationResult;
import com.example.bigbrotherbe.global.jwt.JwtToken;
import com.example.bigbrotherbe.global.jwt.entity.TokenDto;
import com.example.bigbrotherbe.global.security.SecurityConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@RequestMapping(SecurityConfig.SERVER+"/members")
@Tag(name = "멤버", description = "회원가입,로그인 API")
public interface MemberController {

    @Operation(summary = "회원가입", description = "회원가입")
    @PostMapping("/sign-up")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = MemberResponse.class))})
    })
    ResponseEntity<com.example.bigbrotherbe.global.exception.response.ApiResponse<MemberResponse>> signUp(@RequestBody SignUpDto signUpDto);

    @Operation(summary = "로그인")
    @PostMapping("/sign-in")
    ResponseEntity<com.example.bigbrotherbe.global.exception.response.ApiResponse<JwtToken>> signIn(@RequestBody MemberRequest memberRequest);

    @PostMapping("/test")
    @Operation(summary = "test용", description = "현재는 로그인 멤버의 AffiliationRoleLost를 가져오는 값")
    ResponseEntity<com.example.bigbrotherbe.global.exception.response.ApiResponse<AffiliationListDto>> test();

    @GetMapping("/information")
    @Operation(summary = "유저 상세 정보 조회")
    ResponseEntity<com.example.bigbrotherbe.global.exception.response.ApiResponse<MemberInfoResponse>> inquireMemberInfo();

    @GetMapping("/sign-up/emails/verification")
    @Operation(summary = "이메일 중복확인")
    ResponseEntity<com.example.bigbrotherbe.global.exception.response.ApiResponse<EmailVerificationResult>> verificateEmail(@RequestParam(name = "member-email") String email);

    @PostMapping("/sign-up/emails/request-code")
    @Operation(summary = "이메일 인증 코드 요청")
    ResponseEntity<com.example.bigbrotherbe.global.exception.response.ApiResponse<EmailVerificationResult>> sendMessage(@RequestBody EmailRequest emailRequest);

    @GetMapping("/sign-up/emails/verifications")
    @Operation(summary = "이메일 인증 코드 검증")
    ResponseEntity<com.example.bigbrotherbe.global.exception.response.ApiResponse<EmailVerificationResult>> verificationEmail(@RequestParam(name = "email") String email, @RequestParam(name = "code") String code);

    @PatchMapping("/password")
    @Operation(summary = "비밀번호 변경")
    ResponseEntity<com.example.bigbrotherbe.global.exception.response.ApiResponse<Void>> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest);

    @GetMapping("/colleges")
    @Operation(summary = "단과대학 리스트 조회")
    ResponseEntity<com.example.bigbrotherbe.global.exception.response.ApiResponse<List<AffiliationResponse>>> getCollegesList();

    @GetMapping("/departments")
    @Operation(summary = "해당 학과 리스트 조회")
    ResponseEntity<com.example.bigbrotherbe.global.exception.response.ApiResponse<List<AffiliationResponse>>> getDepartmentList(@RequestParam(name = "councilName") String councilName);

    @GetMapping("/refresh")
    @Operation(summary = "refresh 토큰으로 토큰 재 생성 요청")
    ResponseEntity<com.example.bigbrotherbe.global.exception.response.ApiResponse<TokenDto>> refreshToken(@RequestHeader("Authorization") String refreshToken);


    @DeleteMapping
    @Operation(summary = "유저 탈퇴")
ResponseEntity<com.example.bigbrotherbe.global.exception.response.ApiResponse<Void>> memberDeleteSelf();

    @PatchMapping("/information")
    @Operation(summary = "유저 상세 정보 변경")
    ResponseEntity<com.example.bigbrotherbe.global.exception.response.ApiResponse<MemberInfoResponse>> changeMemberInfo(@RequestBody MemberInfoChangeRequest memberInfoChangeRequest);

    @GetMapping()
    @Operation(summary = "유저 전체 조회")
    ResponseEntity<com.example.bigbrotherbe.global.exception.response.ApiResponse<List<MemberInfoResponse>>> inquireAllUserInfo();

    @GetMapping("/{email}")
    @Operation(summary = "유저 이메일을 통한 조회")
    ResponseEntity<com.example.bigbrotherbe.global.exception.response.ApiResponse<MemberInfoResponse>> findUserByEmail(@PathVariable(name = "email") String email);

}
