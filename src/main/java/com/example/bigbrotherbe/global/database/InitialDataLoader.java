package com.example.bigbrotherbe.global.database;

import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.domain.member.repository.AffiliationMemberRepository;
import com.example.bigbrotherbe.domain.member.repository.AffiliationRepository;
import com.example.bigbrotherbe.domain.member.repository.MemberRepository;
import com.example.bigbrotherbe.domain.member.entity.role.Affiliation;
import com.example.bigbrotherbe.domain.member.entity.role.AffiliationMember;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class InitialDataLoader {

    @Bean
    public CommandLineRunner loadData(MemberRepository memberRepository, AffiliationRepository affiliationRepository,
                                      AffiliationMemberRepository affiliationMemberRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (affiliationRepository.findByAffiliationName("응용소프트웨어전공").isEmpty()) {
                affiliationRepository.save(Affiliation.builder().affiliation_id(1L).affiliationName("응용소프트웨어전공").councilType("단과대").build());
            }

            if (affiliationRepository.findByAffiliationName("디지털콘텐츠디자인학과").isEmpty()) {
                affiliationRepository.save(Affiliation.builder().affiliation_id(2L).affiliationName("디지털콘텐츠디자인학과").councilType("학과").build());

            }
            // Check if an admin user already exists
            if (memberRepository.findByUsername("admin").isEmpty()) {
                // Create initial admin user
                Member admin = new Member();
                admin.setPassword(passwordEncoder.encode("admin_password1234!"));
                admin.setEmail("developthreefeet@gmail.com");
                admin.setUsername("admin");
                memberRepository.save(admin);
                Affiliation affiliation = affiliationRepository.findByAffiliationName("응용소프트웨어전공")
                        .orElseThrow(() -> new IllegalArgumentException("잘못된 소속 이름입니다."));

                // AffiliationMember 엔티티 생성
                AffiliationMember affiliationMember = AffiliationMember.builder()
                        .member(admin)
                        .affiliation(affiliation)
                        .role("ROLE_ADMIN")
                        .build();

                affiliationMemberRepository.save(affiliationMember);
            }
        };
    }
}
