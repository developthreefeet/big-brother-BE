package com.example.bigbrotherbe.domain.member.service;

import static com.example.bigbrotherbe.global.email.EmailConfig.AUTH_CODE_PREFIX;

import com.example.bigbrotherbe.domain.member.entity.dto.request.MemberRequest;
import com.example.bigbrotherbe.domain.member.entity.dto.request.SignUpDto;
import com.example.bigbrotherbe.domain.member.entity.dto.response.MemberResponse;
import com.example.bigbrotherbe.domain.member.entity.role.Affiliation;
import com.example.bigbrotherbe.domain.member.entity.role.AffiliationMember;
import com.example.bigbrotherbe.domain.member.repository.AffiliationMemberRepository;
import com.example.bigbrotherbe.domain.member.repository.AffiliationRepository;
import com.example.bigbrotherbe.global.email.EmailVerificationResult;
import com.example.bigbrotherbe.global.email.MailService;
import com.example.bigbrotherbe.global.exception.BusinessException;
import com.example.bigbrotherbe.global.exception.enums.ErrorCode;
import com.example.bigbrotherbe.global.jwt.JwtToken;
import com.example.bigbrotherbe.global.jwt.JwtTokenProvider;
import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.domain.member.repository.MemberRepository;


import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Random;
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
    private final MailService mailService;

//    @Value("${spring.mail.auth-code-expiration-millis}")
    private final long authCodeExpirationMillis = 1800000;


    @Transactional
    @Override
    public MemberResponse userSignUp(SignUpDto signUpDto){
        if (memberRepository.existsByUsername(signUpDto.getEmail())) {
            throw new BusinessException(ErrorCode.EXIST_EMAIL);
        }
        // Password 암호화
        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());
        Member savedMember = memberRepository.save(signUpDto.toEntity(signUpDto,encodedPassword));

        // Affiliation 조회
        Affiliation affiliation = affiliationRepository.findByAffiliationName(signUpDto.getAffiliation())
            .orElseThrow(() -> new BusinessException(ErrorCode.NO_EXIST_AFFILIATION));

        // AffiliationMember 엔티티 생성
        AffiliationMember affiliationMember = AffiliationMember.builder()
            .member(savedMember)
            .affiliation(affiliation)
            .role("ROLE_USER")
            .build();
        affiliationMemberRepository.save(affiliationMember);

        savedMember = memberRepository.findById(savedMember.getId()).orElseThrow(
            () -> new BusinessException(ErrorCode.FAIL_LOAD_MEMBER)
        );

        return MemberResponse.form(savedMember.getId(),savedMember.getUsername(), savedMember.getEmail(), savedMember.getCreate_at(), getMemberRole(savedMember),null);
    }

    @Transactional
    @Override
    public JwtToken userSignIN(String email, String password){
        // 1. 사용자가 입력한 비밀번호와 저장된 비밀번호를 비교
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException(ErrorCode.NO_EXIST_EMAIL));
        log.info("Found member: {}", member);

        if (!passwordEncoder.matches(password, member.getPassword())) {
            log.info("Password mismatch");
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        // 2. 인증 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(email, password);
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



    @Override
    public MemberResponse inquireMemberInfo(String memberEmail) {
        Member member = findByMemberEmail(memberEmail);
        return MemberResponse.builder().email(member.getEmail()).memberName(member.getUsername()).create_at(member.getCreate_at()).update_at(member.getUpdate_at()).roles(getMemberRole(member)
            ).build();
    }



    // 인증코드 요청 및 일시 저장
    @Override
    @Transactional
    public void sendCodeToEmail(String toEmail) {
        String title = "명지대 big brother 이메일 인증 번호";
        String authCode = this.createCode();
        mailService.sendEmail(toEmail, title, authCode);
        // 이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = "AuthCode " + Email / value = AuthCode )
        mailService.saveEmailAuthCode(AUTH_CODE_PREFIX + toEmail, authCode, Duration.ofMillis(this.authCodeExpirationMillis));
    }



    public EmailVerificationResult verifiedCode(String email, String authCode) {
        this.checkDuplicatedEmail(email);
        String redisAuthCode = mailService.getAuthCode(AUTH_CODE_PREFIX + email);
        boolean authResult = redisAuthCode.equals(authCode);

        return EmailVerificationResult.of(authResult);
    }

    // 이메일 중복 체크
    @Override
    public EmailVerificationResult verificateEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            log.debug("MemberServiceImpl.checkDuplicatedEmail exception occur email: {}", email);
            throw new BusinessException(ErrorCode.EXIST_EMAIL);
        }
        return EmailVerificationResult.builder().authResult(true).build();
    }

    /*
    [MEETINGS]
     */
    @Override
    @Transactional(readOnly = true)
    public boolean checkExistAffiliationById(Long affiliationId) {
        return affiliationRepository.existsById(affiliationId);
    }

    @Override
    @Transactional
    public MemberResponse changePasswrd(String memberId, MemberRequest memberRequest) {
        Member member = findByMemberId(memberId);
        member.changePassword(memberRequest.getMemberPass());
        return MemberResponse.form(member.getId(),member.getUsername(), member.getEmail(), member.getCreate_at(),null,member.getPassword());
    }



    private Member findByUserName(String username) {
        return memberRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("잘못된 사용자 이름입니다."));
    }

    private Member findByMemberId(String memberId) {
        return memberRepository.findById(Long.valueOf(memberId)) .orElseThrow(() -> new IllegalArgumentException("잘못된 사용자 아이디입니다."));
    }

    private Member findByMemberEmail(String memberEmail) {
        return memberRepository.findByEmail(memberEmail) .orElseThrow(() -> new BusinessException(ErrorCode.NO_EXIST_EMAIL));

    }

    private void checkDuplicatedEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            log.debug("MemberServiceImpl.checkDuplicatedEmail exception occur email: {}", email);
            throw new BusinessException(ErrorCode.EXIST_EMAIL);
        }
    }
    private String createCode() {
        int lenth = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createCode() exception occur");
            throw new BusinessException(ErrorCode.NO_SUCH_ALGORITHM);
        }
    }


    public List<String> getMemberRole(Member member) {
        return member.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority).toList();
    }
}
