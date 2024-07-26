package com.example.bigbrotherbe.domain.member.repository;

import com.example.bigbrotherbe.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<Member> findByEmail(String email);
}
