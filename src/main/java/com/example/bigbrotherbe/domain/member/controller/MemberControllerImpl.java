package com.example.bigbrotherbe.domain.member.controller;

import com.example.bigbrotherbe.domain.member.entity.dto.request.MemberRequest;
import com.example.bigbrotherbe.domain.member.entity.dto.request.SignUpDto;
import com.example.bigbrotherbe.domain.member.entity.dto.response.MemberResponse;
import com.example.bigbrotherbe.global.email.EmailVerificationResult;
import com.example.bigbrotherbe.global.jwt.JwtToken;
import com.example.bigbrotherbe.global.response.ApiResponse;
import com.example.bigbrotherbe.global.security.SecurityConfig;
import com.example.bigbrotherbe.domain.member.service.MemberService;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberControllerImpl implements MemberController{

    private final MemberService memberService;


    public  ResponseEntity<ApiResponse<MemberResponse>> signUp(SignUpDto signUpDto) {
        MemberResponse memberResponse = memberService.userSignUp(signUpDto);

        // ApiResponse 생성
        ApiResponse<MemberResponse> response = ApiResponse.ok(memberResponse);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/sign-in")
    public JwtToken signIn(@RequestBody MemberRequest memberRequest) {
        String memberEmail = memberRequest.getMemberEmail();
        String password = memberRequest.getMemberPass();
        // 컨트롤러가 없어도 굴러가게 만들어야 하는 데 그러면 Request 객체를 그대로 넘겨주나?
        JwtToken jwtToken = memberService.userSignIN(memberEmail, password);
        log.info("request memberEmail = {}, password = {}", memberEmail, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(),
                jwtToken.getRefreshToken());
        return jwtToken;
    }

    @PostMapping("/test")
    public String test() {
        memberService.makeAffiliation();
        return "완성";
    }

    // member 상세 조회
    @GetMapping
    public ResponseEntity<MemberResponse> inquireMemberInfo(@RequestParam(name = "member_email") String memberEmail) {
        return ResponseEntity.ok(memberService.inquireMemberInfo(memberEmail));
    }

    @PostMapping("/admins")
    public JwtToken adminLogin(@RequestBody MemberRequest memberRequest) {
        String memberEmail = memberRequest.getMemberEmail();
        String password = memberRequest.getMemberPass();
        JwtToken jwtToken = memberService.userSignIN(memberEmail, password);
        log.info("request memberName = {}, password = {}", memberEmail, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(),
                jwtToken.getRefreshToken());
        return jwtToken;
    }

    @PostMapping("/manager")
    public String adminTest() {
        return SecurityConfig.getCurrentUserName();
    }

    // 이메일 중복 확인
    @GetMapping("/sign-up/emails/verification")
    public ResponseEntity<EmailVerificationResult> verificateEmail(@RequestParam(name = "member-email") String email) {
        return ResponseEntity.ok(memberService.verificateEmail(email));
    }

    // 이메일 인증 코드 요구
    @PostMapping("/sign-up/emails/request-code")
    public ResponseEntity<EmailVerificationResult> sendMessage(@RequestBody Map<String, String> email) {
        memberService.sendCodeToEmail(email.get("email"));

        return ResponseEntity.ok(EmailVerificationResult.builder().authResult(true).build());
    }

    @GetMapping("/sign-up/emails/verifications")
    public ResponseEntity<EmailVerificationResult> verificationEmail(@RequestParam(name = "email") String email, @RequestParam(name = "code") String code) {
        return ResponseEntity.ok(memberService.verifiedCode(email, code));
    }

    @PatchMapping()
    public ResponseEntity<MemberResponse> changePassword(@RequestParam(name = "id") String memberId,@RequestBody MemberRequest memberRequest){
        return ResponseEntity.ok(memberService.changePasswrd(memberId,memberRequest));
    }
}
