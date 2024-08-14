package com.example.bigbrotherbe.domain.rule.controller;

import com.example.bigbrotherbe.domain.rule.dto.request.RuleRegisterRequest;
import com.example.bigbrotherbe.domain.rule.dto.request.RuleUpdateRequest;
import com.example.bigbrotherbe.domain.rule.dto.response.RuleResponse;
import com.example.bigbrotherbe.domain.rule.entity.Rule;
import com.example.bigbrotherbe.domain.rule.service.RuleService;
import com.example.bigbrotherbe.global.exception.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.bigbrotherbe.global.constant.Constant.GetContent.PAGE_DEFAULT_VALUE;
import static com.example.bigbrotherbe.global.constant.Constant.GetContent.SIZE_DEFAULT_VALUE;
import static com.example.bigbrotherbe.global.exception.enums.SuccessCode.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rule")
public class RuleController {

    private final RuleService ruleService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> registerRule(@RequestPart(value = "ruleRegisterRequest") RuleRegisterRequest ruleRegisterRequest,
                                                          @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
        ruleService.registerRule(ruleRegisterRequest, multipartFiles);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS));
    }

    @PutMapping("/{ruleId}")
    public ResponseEntity<ApiResponse<Void>> updateRule(@PathVariable("ruleId") Long ruleId,
                                                        @RequestPart(value = "ruleUpdateRequest") RuleUpdateRequest ruleUpdateRequest,
                                                        @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
        ruleService.updateRule(ruleId, ruleUpdateRequest, multipartFiles);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS));
    }

    @DeleteMapping("/{ruleId}")
    public ResponseEntity<ApiResponse<Void>> deleteRule(@PathVariable("ruleId") Long ruleId) {
        ruleService.deleteRule(ruleId);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS));
    }

    @GetMapping("/{ruleId}")
    public ResponseEntity<ApiResponse<RuleResponse>> getRuleById(@PathVariable("ruleId") Long ruleId) {
        RuleResponse ruleResponse = ruleService.getRuleById(ruleId);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, ruleResponse));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<Page<Rule>>> getRuleList(@RequestParam(name = "affiliationId") Long affiliationId,
                                                               @RequestParam(name = "page", defaultValue = PAGE_DEFAULT_VALUE) int page,
                                                               @RequestParam(name = "size", defaultValue = SIZE_DEFAULT_VALUE) int size,
                                                               @RequestParam(name = "search", required = false) String search) {
        Page<Rule> rulePage;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        if (search != null && !search.isEmpty()) {
            rulePage = ruleService.searchRules(affiliationId, search, pageable);
        } else {
            rulePage = ruleService.getRules(affiliationId, pageable);
        }
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, rulePage));
    }
}
