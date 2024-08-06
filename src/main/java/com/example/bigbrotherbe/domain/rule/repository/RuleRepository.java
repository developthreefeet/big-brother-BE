package com.example.bigbrotherbe.domain.rule.repository;

import com.example.bigbrotherbe.domain.meetings.entity.Meetings;
import com.example.bigbrotherbe.domain.rule.entity.Rule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule, Long> {
    Page<Rule> findByAffiliationId(Long affiliationId, Pageable pageable);

    Page<Rule> findByAffiliationIdAndTitleContaining(Long affiliationId, String title, Pageable pageable);
}
