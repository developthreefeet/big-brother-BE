package com.example.bigbrotherbe.domain.transactions.service;

import com.example.bigbrotherbe.domain.transactions.dto.request.TransactionsUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

public interface TransactionsService {
    void register(MultipartFile multipartFile, Long affiliationId);

    void update(Long transactionsId, TransactionsUpdateRequest transactionsUpdateRequest);
}
