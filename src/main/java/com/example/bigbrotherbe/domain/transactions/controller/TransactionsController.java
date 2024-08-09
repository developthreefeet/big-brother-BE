package com.example.bigbrotherbe.domain.transactions.controller;

import com.example.bigbrotherbe.domain.transactions.service.TransactionsServiceImpl;
import com.example.bigbrotherbe.global.exception.response.ApiResponse;
import com.example.bigbrotherbe.global.ocr.dto.OcrDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import static com.example.bigbrotherbe.global.exception.enums.SuccessCode.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/big-brother/transactions/{affiliationId}")
public class TransactionsController {

    private final TransactionsServiceImpl transactionsService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> registerTransaction(@PathVariable("affiliationId") Long affiliationId,
                                                                 @RequestPart(value = "file") MultipartFile multipartFile) {
        transactionsService.register(multipartFile, affiliationId);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS));
    }
}
