package com.example.bigbrotherbe.domain.member.service;

import static com.example.bigbrotherbe.global.email.EmailConfig.AUTH_CODE_PREFIX;

import com.example.bigbrotherbe.domain.member.entity.dto.request.MemberRequest;
import com.example.bigbrotherbe.domain.member.entity.dto.request.SignUpDto;
import com.example.bigbrotherbe.domain.member.entity.dto.response.MemberInfoResponse;
import com.example.bigbrotherbe.domain.member.entity.dto.response.MemberResponse;
import com.example.bigbrotherbe.domain.member.entity.role.Affiliation;
import com.example.bigbrotherbe.domain.member.entity.role.AffiliationMap;
import com.example.bigbrotherbe.domain.member.entity.role.AffiliationMember;
import com.example.bigbrotherbe.domain.member.repository.AffiliationMemberRepository;
import com.example.bigbrotherbe.domain.member.repository.AffiliationRepository;
import com.example.bigbrotherbe.domain.member.util.MemberChecker;
import com.example.bigbrotherbe.domain.member.util.MemberLoader;
import com.example.bigbrotherbe.global.email.EmailVerificationResult;
import com.example.bigbrotherbe.global.email.MailService;
import com.example.bigbrotherbe.global.exception.BusinessException;
import com.example.bigbrotherbe.global.exception.enums.ErrorCode;
import com.example.bigbrotherbe.global.jwt.AuthUtil;
import com.example.bigbrotherbe.global.jwt.JwtToken;
import com.example.bigbrotherbe.global.jwt.JwtTokenProvider;
import com.example.bigbrotherbe.domain.member.entity.Member;


import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
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
public class MemberServiceImpl implements MemberService {

    private final AffiliationRepository affiliationRepository;
    private final AffiliationMemberRepository affiliationMemberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final AuthUtil authUtil;
    private final MemberLoader memberLoader;
    private final MemberChecker memberChecker;
    //    @Value("${spring.mail.auth-code-expiration-millis}")
    private final long authCodeExpirationMillis = 1800000;


    @Transactional
    @Override
    public MemberResponse userSignUp(SignUpDto signUpDto) {
        memberChecker.checkExistUserEmail(signUpDto.getEmail());

        // Password 암호화후 저장
        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());
        Member savedMember = memberLoader.saveMember(signUpDto.toEntity(signUpDto, encodedPassword));


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

        savedMember = memberLoader.getMember(savedMember.getId());

        return MemberResponse.form(savedMember.getId(), savedMember.getUsername(), savedMember.getEmail(), savedMember.getCreateAt());
    }

    @Transactional
    @Override
    public JwtToken userSignIN(String email, String password) {
        // 1. 사용자가 입력한 비밀번호와 저장된 비밀번호를 비교
        Member member = memberLoader.getMemberByEmail(email);

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
    public MemberInfoResponse inquireMemberInfo() {
        Member member = authUtil.getLoginMember();
        return MemberInfoResponse
            .builder()
            .email(member.getEmail())
            .memberName(member.getUsername())
            .createAt(member.getCreateAt())
            .updateAt(member.getUpdateAt())
            .affiliationMap(getMemberAffiliationRoleList())
            .build();
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
        memberChecker.checkDuplicatedEmail(email);
        String redisAuthCode = mailService.getAuthCode(AUTH_CODE_PREFIX + email);
        boolean authResult = redisAuthCode.equals(authCode);

        return EmailVerificationResult.of(authResult);
    }

    // 이메일 중복 체크
    @Override
    public EmailVerificationResult verificateEmail(String email) {
        Member member = memberLoader.findByMemberEmail(email);
        if (!Objects.isNull(member)) {
            throw new BusinessException(ErrorCode.EXIST_EMAIL);
        }
        return EmailVerificationResult.builder().authResult(true).build();
    }

    /*
    거의 모든 도메인 validation으로 사용
     */
    @Override
    @Transactional(readOnly = true)
    public boolean checkExistAffiliationById(Long affiliationId) {
        return affiliationRepository.existsById(affiliationId);
    }

    @Override
    @Transactional
    public void changePasswrd(String password) {
        Member member = authUtil.getLoginMember();
        member.changePassword( passwordEncoder.encode(password));
    }

    @Override
    @Transactional
    public void makeAffiliation() {
    affiliationRepository.save(Affiliation.builder().affiliation_id(1).affiliationName("총학").build());
    }

    @Override
    public AffiliationMap getMemberAffiliationRoleList() {
        Member member = authUtil.getLoginMember();
        List<AffiliationMember> affiliationMemberList= affiliationMemberRepository.findAllByMemberId(member.getId());
        return  transforAffiliationRole(member.getUsername(),affiliationMemberList);
    }

    private AffiliationMap transforAffiliationRole(String userName,List<AffiliationMember> affiliationMemberList) {
        AffiliationMap affiliationMap = new AffiliationMap(userName);
        for(AffiliationMember affiliationMember : affiliationMemberList){
            affiliationMap.addPosition(affiliationMember.getAffiliation().getAffiliationName(),affiliationMember.getRole());
        }
        return affiliationMap;
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
}
