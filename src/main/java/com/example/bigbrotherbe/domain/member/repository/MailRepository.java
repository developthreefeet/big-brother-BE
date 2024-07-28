package com.example.bigbrotherbe.domain.member.repository;

import com.example.bigbrotherbe.domain.member.entity.EMailVerification;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepository extends JpaRepository<EMailVerification, Long> {

    void deleteByEmailAddress(String emailAddress);

    Optional<EMailVerification> findByEmailAddress(String emailAddress);
}
