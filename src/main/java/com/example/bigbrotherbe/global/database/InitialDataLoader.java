package com.example.bigbrotherbe.global.database;

import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.domain.member.entity.enums.AffiliationCode;
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
            for(AffiliationCode affiliationCode : AffiliationCode.values()){
                if(affiliationRepository.findByName(affiliationCode.getName()).isEmpty()){
                    affiliationRepository.save(Affiliation.toEntity(affiliationCode));
                }
            }
            // Check if an admin user already exists
            if (memberRepository.findByUsername("admin").isEmpty()) {
                // Create initial admin user
                Member admin = new Member();
                admin.setPassword(passwordEncoder.encode("admin_password1234!"));
                admin.setEmail("developthreefeet@gmail.com");
                admin.setUsername("admin");
                memberRepository.save(admin);


                Affiliation college = Affiliation.toEntity(AffiliationCode.ICT);
                // AffiliationMember 엔티티 생성
                affiliationMemberRepository.save(AffiliationMember
                    .builder()
                    .member(admin)
                    .affiliation(college)
                    .role("ROLE_ADMIN")
                    .build());

                Affiliation affiliation = Affiliation.toEntity(AffiliationCode.SOFTWARE_APPLICATIONS);
                affiliationMemberRepository.save(AffiliationMember
                    .builder()
                    .member(admin)
                    .affiliation(affiliation)
                    .role("ROLE_ADMIN")
                    .build());

            }
        };
    }
}
