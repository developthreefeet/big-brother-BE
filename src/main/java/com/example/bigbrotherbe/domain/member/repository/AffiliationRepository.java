package com.example.bigbrotherbe.domain.member.repository;

import com.example.bigbrotherbe.domain.member.entity.role.Affiliation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AffiliationRepository extends JpaRepository<Affiliation, Long> {
    Optional<Affiliation> findByName(String name);
}
