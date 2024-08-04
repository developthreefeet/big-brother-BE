package com.example.bigbrotherbe.domain.faq.repository;

import com.example.bigbrotherbe.domain.faq.entity.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FAQRepository extends JpaRepository<FAQ, Long> {
    Optional<FAQ> findByTitle(String faqTitle);
}
