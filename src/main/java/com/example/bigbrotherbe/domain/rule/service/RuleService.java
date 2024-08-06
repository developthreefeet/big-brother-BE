package com.example.bigbrotherbe.domain.rule.service;

import com.example.bigbrotherbe.domain.rule.dto.request.RuleRegisterRequest;
import com.example.bigbrotherbe.domain.rule.dto.request.RuleUpdateRequest;
import com.example.bigbrotherbe.domain.rule.dto.response.RuleResponse;
import com.example.bigbrotherbe.domain.rule.entity.Rule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RuleService {
    void registerRule(RuleRegisterRequest ruleRegisterRequest, List<MultipartFile> multipartFiles);

    void updateRule(Long ruleId, RuleUpdateRequest ruleUpdateRequest, List<MultipartFile> multipartFiles);

    void deleteRule(Long ruleId);

    RuleResponse getRuleById(Long ruleId);

    Page<Rule> getRules(Long affiliationId, Pageable pageable);

    Page<Rule> searchRules(Long affiliationId, String title, Pageable pageable);
}
