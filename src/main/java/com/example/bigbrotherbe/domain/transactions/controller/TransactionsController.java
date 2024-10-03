package com.example.bigbrotherbe.domain.transactions.controller;

import com.example.bigbrotherbe.domain.transactions.dto.response.TransactionsResponse;
import com.example.bigbrotherbe.domain.transactions.service.TransactionsService;
import com.example.bigbrotherbe.common.exception.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static com.example.bigbrotherbe.common.exception.enums.SuccessCode.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionsController {

    private final TransactionsService transactionsService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionsResponse>>> getTransactions(@RequestParam("affiliation") String affiliation,
                                                                                   @RequestParam("year") int year,
                                                                                   @RequestParam("month") int month) {
        List<TransactionsResponse> transactionsList = transactionsService.getTransactionsWithMonth(year, month, affiliation);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, transactionsList));
    }
}
