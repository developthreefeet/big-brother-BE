package com.example.bigbrotherbe.domain.rule.repository;

import com.example.bigbrotherbe.domain.rule.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule, Long> {
}
