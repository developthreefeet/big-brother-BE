package com.example.bigbrotherbe.member.service;

import com.example.bigbrotherbe.global.jwt.JwtToken;
import com.example.bigbrotherbe.global.jwt.JwtTokenProvider;
import com.example.bigbrotherbe.member.entity.Member;
import com.example.bigbrotherbe.member.entity.dto.request.MemberDto;
import com.example.bigbrotherbe.member.entity.dto.request.SignUpDto;
import com.example.bigbrotherbe.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    @Transactional
    @Override
    public JwtToken signIn(String username, String password){

        // 1. 사용자가 입력한 비밀번호와 저장된 비밀번호를 비교
        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("잘못된 사용자 이름입니다."));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        // 2. 인증 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken = new
            UsernamePasswordAuthenticationToken(username, password);

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member에 대한 검증 진행
        Authentication authentication =
            authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        return jwtTokenProvider.generateToken(authentication);
    }
    @Transactional
    @Override
    public MemberDto signUp(SignUpDto signUpDto){
        if (memberRepository.existsByUsername(signUpDto.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 사용자 이름입니다.");
        }
        // Password 암호화
        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());
        List<String> roles = new ArrayList<>();
        roles.add("USER");  // USER 권한 부여
        return MemberDto.toDto(memberRepository.save(signUpDto.toEntity(signUpDto,encodedPassword, roles)));
    }
}
