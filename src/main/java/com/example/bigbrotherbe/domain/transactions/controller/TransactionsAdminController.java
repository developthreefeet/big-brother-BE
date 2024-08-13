package com.example.bigbrotherbe.domain.transactions.controller;


import com.example.bigbrotherbe.domain.transactions.dto.request.TransactionsUpdateRequest;
import com.example.bigbrotherbe.domain.transactions.dto.response.TransactionsResponse;
import com.example.bigbrotherbe.domain.transactions.service.TransactionsService;
import com.example.bigbrotherbe.global.exception.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.bigbrotherbe.global.exception.enums.SuccessCode.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/transactions")
public class TransactionsAdminController {

    private final TransactionsService transactionsService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> registerTransaction(@RequestParam("affiliationId") Long affiliationId,
                                                                 @RequestPart(value = "file") MultipartFile multipartFile) {
        transactionsService.register(multipartFile, affiliationId);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS));
    }

    @PutMapping("/{transactionsId}")
    public ResponseEntity<ApiResponse<Void>> updateTransactions(@PathVariable("transactionsId") Long transactionsId,
                                                                @RequestBody TransactionsUpdateRequest transactionsUpdateRequest) {
        transactionsService.update(transactionsId, transactionsUpdateRequest);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionsResponse>>> getTransactions(@RequestParam("affiliationId") Long affiliationId,
                                                                                   @RequestParam("year") int year,
                                                                                   @RequestParam("month") int month) {
        List<TransactionsResponse> transactionsList = transactionsService.getTransactionsWithMonth(year, month, affiliationId);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, transactionsList));
    }
}
