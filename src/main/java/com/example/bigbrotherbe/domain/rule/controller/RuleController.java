package com.example.bigbrotherbe.domain.rule.controller;

import com.example.bigbrotherbe.domain.meetings.dto.request.MeetingsRegisterRequest;
import com.example.bigbrotherbe.domain.meetings.dto.request.MeetingsUpdateRequest;
import com.example.bigbrotherbe.domain.rule.dto.request.RuleRegisterRequest;
import com.example.bigbrotherbe.domain.rule.dto.request.RuleUpdateRequest;
import com.example.bigbrotherbe.domain.rule.service.RuleService;
import com.example.bigbrotherbe.global.exception.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.bigbrotherbe.global.exception.enums.SuccessCode.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/big-brother/rule")
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

}
