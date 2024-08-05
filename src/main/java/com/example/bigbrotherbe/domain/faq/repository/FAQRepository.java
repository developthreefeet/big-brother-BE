package com.example.bigbrotherbe.domain.faq.repository;

import com.example.bigbrotherbe.domain.faq.entity.FAQ;
import com.example.bigbrotherbe.domain.meetings.entity.Meetings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FAQRepository extends JpaRepository<FAQ, Long> {
//    Optional<FAQ> findByTitle(String faqTitle);
    Page<FAQ> findByAffiliationId(Long affiliationId, Pageable pageable);

    Page<FAQ> findByAffiliationIdAndTitleContaining(Long affiliationId, String title, Pageable pageable);
}
