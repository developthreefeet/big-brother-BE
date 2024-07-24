package com.example.bigbrotherbe.domain.member.service;

import com.example.bigbrotherbe.domain.member.entity.dto.request.SignUpDto;
import com.example.bigbrotherbe.domain.member.entity.dto.response.MemberResponse;
import com.example.bigbrotherbe.domain.member.entity.role.Affiliation;
import com.example.bigbrotherbe.domain.member.entity.role.AffiliationMember;
import com.example.bigbrotherbe.domain.member.repository.AffiliationMemberRepository;
import com.example.bigbrotherbe.domain.member.repository.AffiliationRepository;
import com.example.bigbrotherbe.global.jwt.JwtToken;
import com.example.bigbrotherbe.global.jwt.JwtTokenProvider;
import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.domain.member.repository.MemberRepository;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final AffiliationRepository affiliationRepository;
    private final AffiliationMemberRepository affiliationMemberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    @Transactional
    @Override
    public JwtToken userSignIN(String username, String password){
        // 1. 사용자가 입력한 비밀번호와 저장된 비밀번호를 비교
        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("잘못된 사용자 이름입니다."));
        log.info("Found member: {}", member);

        if (!passwordEncoder.matches(password, member.getPassword())) {
            log.info("Password mismatch");
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        // 2. 인증 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(username, password);
        log.info("Created authentication token");

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member에 대한 검증 진행
        Authentication authentication;
        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        } catch (Exception e) {
            log.info("Authentication failed", e);
            throw e;
        }

        log.info("Authenticated: {}", authentication);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        return jwtTokenProvider.generateToken(authentication);
    }


    @Transactional
    @Override
    public MemberResponse userSignUp(SignUpDto signUpDto){
        if (memberRepository.existsByUsername(signUpDto.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 사용자 이름입니다.");
        }
        // Password 암호화
        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());
        Member savedMember = memberRepository.save(signUpDto.toEntity(signUpDto,encodedPassword));

        // Affiliation 조회
        Affiliation affiliation = affiliationRepository.findByAffiliationName(signUpDto.getAffiliation())
            .orElseThrow(() -> new IllegalArgumentException("잘못된 소속 이름입니다."));

        // AffiliationMember 엔티티 생성
        AffiliationMember affiliationMember = AffiliationMember.builder()
            .member(savedMember)
            .affiliation(affiliation)
            .role("ROLE_USER")
            .build();
        affiliationMemberRepository.save(affiliationMember);

        savedMember = memberRepository.findById(savedMember.getId()).orElseThrow(
            () -> new IllegalArgumentException("Member를 다시 로드할 수 없습니다.")
        );
//        List<String> roles = savedMember.getAuthorities().stream()
//            .map(GrantedAuthority::getAuthority)
//            .toList();

        return MemberResponse.form(savedMember.getId(),savedMember.getUsername(), savedMember.getEmail(), savedMember.getCreate_at());
    }

    @Override
    public MemberResponse inquireMemberInfo(String memberName) {
        Member member = findByUserName(memberName);
        return MemberResponse.builder().id(member.getId()).memberName(member.getUsername()).create_at(member.getCreate_at()).update_at(member.getUpdate_at()).roles(member.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList()).build();
    }

    private Member findByUserName(String username) {
        return memberRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("잘못된 사용자 이름입니다."));
    }

}
