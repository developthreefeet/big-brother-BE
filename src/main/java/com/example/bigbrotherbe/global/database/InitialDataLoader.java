package com.example.bigbrotherbe.global.database;

import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.domain.member.repository.AffiliationMemberRepository;
import com.example.bigbrotherbe.domain.member.repository.AffiliationRepository;
import com.example.bigbrotherbe.domain.member.repository.MemberRepository;
import com.example.bigbrotherbe.domain.member.entity.role.Affiliation;
import com.example.bigbrotherbe.domain.member.entity.role.AffiliationMember;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class InitialDataLoader {

    @Bean
    public CommandLineRunner loadData(MemberRepository memberRepository, AffiliationRepository affiliationRepository,
                                      AffiliationMemberRepository affiliationMemberRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if an admin user already exists
            if (memberRepository.findByUsername("admin").isEmpty()) {
                // Create initial admin user
                Member admin = new Member();
                admin.setUsername("admin");
                admin.setEmail("admin");
                admin.setPassword(passwordEncoder.encode("admin_password"));
                memberRepository.save(admin);
                Affiliation affiliation = affiliationRepository.findByAffiliationName("총학")
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
