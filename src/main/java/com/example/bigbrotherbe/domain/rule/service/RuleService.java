package com.example.bigbrotherbe.domain.rule.service;

import com.example.bigbrotherbe.domain.rule.dto.request.RuleRegisterRequest;
import com.example.bigbrotherbe.domain.rule.dto.request.RuleUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RuleService {
    void registerRule(RuleRegisterRequest ruleRegisterRequest, List<MultipartFile> multipartFiles);

    void updateRule(Long ruleId, RuleUpdateRequest ruleUpdateRequest, List<MultipartFile> multipartFiles);
}
