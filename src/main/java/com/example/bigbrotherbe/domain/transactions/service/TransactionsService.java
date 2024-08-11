package com.example.bigbrotherbe.domain.transactions.service;

import com.example.bigbrotherbe.domain.transactions.dto.request.TransactionsUpdateRequest;
import com.example.bigbrotherbe.domain.transactions.dto.response.TransactionsResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TransactionsService {
    void register(MultipartFile multipartFile, Long affiliationId);

    void update(Long transactionsId, TransactionsUpdateRequest transactionsUpdateRequest);

    List<TransactionsResponse> getTransactionsWithMonth(int year, int month, Long affiliationId);
}
