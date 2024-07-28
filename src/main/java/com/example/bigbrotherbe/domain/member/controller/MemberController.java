package com.example.bigbrotherbe.domain.member.controller;

import com.example.bigbrotherbe.domain.member.entity.dto.request.MemberRequest;
import com.example.bigbrotherbe.domain.member.entity.dto.request.SignUpDto;
import com.example.bigbrotherbe.domain.member.entity.dto.response.MemberResponse;
import com.example.bigbrotherbe.global.email.EmailVerificationResult;
import com.example.bigbrotherbe.global.jwt.JwtToken;
import com.example.bigbrotherbe.global.security.SecurityConfig;
import com.example.bigbrotherbe.domain.member.service.MemberService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/members")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-in")
    public JwtToken signIn(@RequestBody MemberRequest memberRequest) {
        String username = memberRequest.getMemberName();
        String password = memberRequest.getMemberPass();
        JwtToken jwtToken = memberService.userSignIN(username, password);
        log.info("request username = {}, password = {}", username, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(),
            jwtToken.getRefreshToken());
        return jwtToken;
    }

    @PostMapping("/test")
    public String test() {
//        return "suess";
        return SecurityConfig.getCurrentUserName();
    }

        @PostMapping("/sign-up")
        public ResponseEntity<MemberResponse> signUp(@RequestBody SignUpDto signUpDto) {
            return ResponseEntity.ok(memberService.userSignUp(signUpDto));
        }

    @GetMapping("/{member_name}")
    public ResponseEntity<MemberResponse> inquireMemberInfo(@PathVariable String member_name){
        return ResponseEntity.ok(memberService.inquireMemberInfo(member_name));
    }

    @PostMapping("/admins")
    public JwtToken adminLogin(@RequestBody MemberRequest memberRequest) {
        String memberName = memberRequest.getMemberName();
        String password = memberRequest.getMemberPass();
        JwtToken jwtToken = memberService.userSignIN(memberName, password);
        log.info("request memberName = {}, password = {}", memberName, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(),
            jwtToken.getRefreshToken());
        return jwtToken;
    }

    @PostMapping("/manager")
    public String adminTest() {
        return SecurityConfig.getCurrentUserName();
    }

    @PostMapping("/emails/verification-requests")
    public ResponseEntity<MemberResponse> sendMessage(@RequestBody Map<String,String> email){
        memberService.sendCodeToEmail(email.get("email"));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/emails/verifications")
    public ResponseEntity<EmailVerificationResult> verificationEmail(@RequestParam(name = "email") String email, @RequestParam(name = "code") String code){
        return ResponseEntity.ok(memberService.verifiedCode(email, code)) ;
    }

}
