package com.example.bigbrotherbe.domain.transactions.service;

import org.springframework.web.multipart.MultipartFile;

public interface TransactionsService {
    void register(MultipartFile multipartFile, Long affiliationId);
}
