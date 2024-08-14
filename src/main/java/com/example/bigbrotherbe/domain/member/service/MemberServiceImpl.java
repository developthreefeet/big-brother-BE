package com.example.bigbrotherbe.domain.member.service;


import com.example.bigbrotherbe.domain.member.dto.request.SignUpDto;
import com.example.bigbrotherbe.domain.member.dto.response.AffiliationResponse;
import com.example.bigbrotherbe.domain.member.dto.response.MemberInfoResponse;
import com.example.bigbrotherbe.domain.member.dto.response.MemberResponse;
import com.example.bigbrotherbe.domain.member.entity.enums.AffiliationCode;
import com.example.bigbrotherbe.domain.member.entity.role.Affiliation;
import com.example.bigbrotherbe.domain.member.entity.role.AffiliationListDto;
import com.example.bigbrotherbe.domain.member.entity.role.AffiliationMember;
import com.example.bigbrotherbe.domain.member.repository.AffiliationMemberRepository;
import com.example.bigbrotherbe.domain.member.repository.AffiliationRepository;
import com.example.bigbrotherbe.domain.member.util.MemberChecker;
import com.example.bigbrotherbe.domain.member.util.MemberDeleter;
import com.example.bigbrotherbe.domain.member.util.MemberLoader;
import com.example.bigbrotherbe.global.email.EmailVerificationResult;
import com.example.bigbrotherbe.global.email.MailService;
import com.example.bigbrotherbe.global.exception.BusinessException;
import com.example.bigbrotherbe.global.exception.enums.ErrorCode;
import com.example.bigbrotherbe.global.jwt.AuthUtil;
import com.example.bigbrotherbe.global.jwt.JwtToken;
import com.example.bigbrotherbe.global.jwt.JwtTokenProvider;
import com.example.bigbrotherbe.domain.member.entity.Member;


import com.example.bigbrotherbe.global.jwt.entity.TokenDto;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    private final MemberDeleter memberDeleter;

    //    @Value("${spring.mail.auth-code-expiration-millis}")
    private final long authCodeExpirationMillis = 1800000;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MemberResponse userSignUp(SignUpDto signUpDto) {
        memberChecker.checkExistUserEmail(signUpDto.getEmail());

        // Password 암호화후 저장
        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());
        Member savedMember = memberLoader.saveMember(signUpDto.toEntity(signUpDto, encodedPassword));


        // Affiliation 조회
        log.info(signUpDto.getCollege() + " " + signUpDto.getAffiliation());
        Affiliation college = affiliationRepository.findByName(signUpDto.getCollege())
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_EXIST_AFFILIATION));

        // AffiliationMember 엔티티 생성
        AffiliationMember memberCollage = AffiliationMember.builder()
                .member(savedMember)
                .affiliation(college)
                .role("ROLE_USER")
                .build();
        affiliationMemberRepository.save(memberCollage);

        Affiliation affiliation = affiliationRepository.findByName(signUpDto.getAffiliation())
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_EXIST_AFFILIATION));

        AffiliationMember memberAffiliation = AffiliationMember.builder()
                .member(savedMember)
                .affiliation(affiliation)
                .role("ROLE_USER")
                .build();

        affiliationMemberRepository.save(memberAffiliation);

        savedMember = memberLoader.getMember(savedMember.getId());

        return MemberResponse.form(savedMember.getId(), savedMember.getUsername(), savedMember.getEmail(), savedMember.getCreateAt(), college.getName(), affiliation.getName());
    }

    @Transactional
    @Override
    public JwtToken userSignIN(String email, String password) {
        // 1. 사용자가 입력한 비밀번호와 저장된 비밀번호를 비교
        Member member = memberLoader.findByMemberEmail(email);

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new BusinessException(ErrorCode.MISMATCH_PASSWORD);
        }

        // 2. 인증 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member에 대한 검증 진행
        Authentication authentication;
        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        } catch (Exception e) {
            log.info("Authentication failed", e);
            throw e;
        }


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
                .affiliationListDto(getMemberAffiliationRoleList())
                .build();
    }


    // 인증코드 요청 및 일시 저장
    @Override
    @Transactional
    public void sendCodeToEmail(String toEmail) {
        mailService.checkPresentEmail(toEmail);
        String title = "명지대 big brother 이메일 인증 번호";
        String authCode = this.createCode();
        mailService.sendEmail(toEmail, title, authCode);
        // 이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = "AuthCode " + Email / value = AuthCode )
        mailService.saveEmailAuthCode(toEmail, authCode, Duration.ofMillis(this.authCodeExpirationMillis));
    }


    public EmailVerificationResult verifiedCode(String email, String authCode) {
        memberChecker.checkDuplicatedEmail(email);
        String redisAuthCode = mailService.getAuthCode(email);
        boolean authResult = redisAuthCode.equals(authCode);
        if (!authResult) {
            throw new BusinessException(ErrorCode.MISMATCH_VERIFIED_CODE);
        }
        return EmailVerificationResult.of(authResult);
    }

    // 이메일 중복 체크
    @Override
    public EmailVerificationResult verificateEmail(String email) {

        if (memberLoader.findByMemberEmailForCheck(email).isPresent()) {
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
    public void changePasswrd(String email, String password) {
        Member member = memberLoader.findByMemberEmail(email);
        member.changePassword(passwordEncoder.encode(password));
    }

    @Override
    @Transactional
    public void makeAffiliation() {
        affiliationRepository.save(Affiliation.builder().affiliation_id(1L).name("총학").build());
    }

    @Override
    public List<AffiliationResponse> getColleges() {
        return Arrays.stream(AffiliationCode.values())
                .filter(code -> code.getCouncilType().equals("단과대"))
                .map(code -> AffiliationResponse.fromAffiliationResponse(code.getVal(), code.getCouncilName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<AffiliationResponse> getDepartments(String councilName) {
        return AffiliationCode.getDepartmentsByCollegeName(councilName);
    }


    @Override
    public TokenDto refreshToken(String refreshToken) {
        System.out.println(refreshToken);
        String resolveToken = resolveToken(refreshToken);

        if (jwtTokenProvider.validateToken(resolveToken)) {
            // 리프레시 토큰이 유효한 경우 새로운 엑세스 토큰 발급
            String newAccessToken = jwtTokenProvider.createTokenByRefreshToken(resolveToken);

            // 새 엑세스 토큰으로 인증 설정
            Authentication authentication = jwtTokenProvider.getAuthentication(newAccessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return TokenDto
                    .builder()
                    .accessToken(newAccessToken)
                    .refreshToken(resolveToken)
                    .build();
        } else
            throw new BusinessException(ErrorCode.REFRESH_Token_Expired);
    }

    @Override
    @Transactional()
    public void deleteSelf() {
        Member member = authUtil.getLoginMember();
        memberDeleter.deleteMember(member);
    }

    @Override
    public MemberInfoResponse changeMemberInfo(String username) {
        Member member = authUtil.getLoginMember();
        member.changeName(username);
        return MemberInfoResponse
                .builder()
                .email(member.getEmail())
                .memberName(member.getUsername())
                .createAt(member.getCreateAt())
                .updateAt(member.getUpdateAt())
                .affiliationListDto(getMemberAffiliationRoleList())
                .build();
    }

    @Override
    public List<MemberInfoResponse> inquireAllMemberInfo() {
        List<MemberInfoResponse> memberInfoResponseList = new ArrayList<>();
        for(Member member: memberLoader.getAllMember()){
            if(distinguishUser(member.getAffiliations())){
            memberInfoResponseList.add(MemberInfoResponse
                .builder()
                .email(member.getEmail())
                .memberName(member.getUsername())
                .createAt(member.getCreateAt())
                .updateAt(member.getUpdateAt())
                .affiliationListDto(getMemberAffiliationRoleList())
                .build());
            }
        }
        return memberInfoResponseList;
    }

    private boolean distinguishUser(List<AffiliationMember> affiliations) {
        for(AffiliationMember affiliationMember : affiliations){
            if(!"ROLE_USER".equals(affiliationMember.getRole())){
                return false;
            }
        }
        return true;
    }


    @Override
    public AffiliationListDto getMemberAffiliationRoleList() {
        Member member = authUtil.getLoginMember();
        List<AffiliationMember> affiliationMemberList = affiliationMemberRepository.findAllByMemberId(member.getId());
        return affiliationListToEntity(member.getUsername(), affiliationMemberList);
    }


    private AffiliationListDto affiliationListToEntity(String userName, List<AffiliationMember> affiliationMemberList) {
        AffiliationListDto affiliationListDto = new AffiliationListDto(userName);

        for (AffiliationMember affiliationMember : affiliationMemberList) {
            Affiliation affiliation = affiliationMember.getAffiliation();
            affiliationListDto.addAffiliation(affiliation.getCouncilType(), affiliation.getName(), affiliationMember.getRole());
        }
        return affiliationListDto;
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

    private String resolveToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        throw new BusinessException(ErrorCode.ILLEGAL_HEADER_PATTERN);
    }

}
