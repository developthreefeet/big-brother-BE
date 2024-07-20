package com.example.bigbrotherbe.global.database;

import com.example.bigbrotherbe.member.entity.Member;
import com.example.bigbrotherbe.member.repository.MemberRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class InitialDataLoader {

    @Bean
    public CommandLineRunner loadData(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if an admin user already exists
            if (memberRepository.findByUsername("admin").isEmpty()) {
                // Create initial admin user
                Member admin = new Member();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin_password"));
                admin.setRoles(List.of("ADMIN"));
                memberRepository.save(admin);
            }
        };
    }
}
