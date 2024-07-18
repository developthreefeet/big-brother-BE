package com.example.bigbrotherbe.member.controller;

import com.example.bigbrotherbe.global.jwt.JwtToken;
import com.example.bigbrotherbe.global.security.SecurityConfig;
import com.example.bigbrotherbe.member.entity.dto.request.MemberDto;
import com.example.bigbrotherbe.member.entity.dto.request.MemberRequest;
import com.example.bigbrotherbe.member.entity.dto.request.SignUpDto;
import com.example.bigbrotherbe.member.service.MemberService;
import com.example.bigbrotherbe.member.service.MemberServiceImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/members")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService ;

    @PostMapping("/sign-in")
    public JwtToken signIn(@RequestBody MemberRequest memberRequest) {
        String username = memberRequest.getMemberName();
        String password = memberRequest.getMemberPass();
        JwtToken jwtToken = memberService.signIn(username, password);
        log.info("request username = {}, password = {}", username, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        return jwtToken;
    }
    @PostMapping("/test")
    public String test(){
//        return "suess";
        return SecurityConfig.getCurrentUserName();
    }

    @PostMapping("/sign-up")
    public ResponseEntity<MemberDto> signUp(@RequestBody SignUpDto signUpDto){
        return ResponseEntity.ok(memberService.signUp(signUpDto));
    }
}
