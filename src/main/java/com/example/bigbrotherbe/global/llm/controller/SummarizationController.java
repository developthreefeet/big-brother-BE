package com.example.bigbrotherbe.global.llm.controller;
import com.example.bigbrotherbe.domain.campusNotice.dto.CampusNoticeResponse;
import com.example.bigbrotherbe.domain.campusNotice.service.CampusNoticeService;
import com.example.bigbrotherbe.global.common.exception.response.ApiResponse;
import com.example.bigbrotherbe.global.llm.dto.SummarizeRequest;
import com.example.bigbrotherbe.global.llm.service.BedrockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.bigbrotherbe.global.common.exception.enums.SuccessCode.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/summarize")
public class SummarizationController {
    private final BedrockService bedrockService;
    private final CampusNoticeService campusNoticeService;

    @GetMapping()
    public ResponseEntity<ApiResponse<String>> getSummarizeById(@RequestBody SummarizeRequest summarizeRequest) {
        CampusNoticeResponse campusNoticeResponse = campusNoticeService.getCampusNoticeById(summarizeRequest.getCampusNoticeId());
        String content = campusNoticeResponse.getContent();
        String response =  bedrockService.getSummary(content);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, response));
    }
}