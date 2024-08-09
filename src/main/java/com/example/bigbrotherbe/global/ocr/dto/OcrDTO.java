package com.example.bigbrotherbe.global.ocr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OcrDTO {
    private List<String[]> parseTransactions;
    private String parseAccountNumber;

    public static OcrDTO fromOcrResponse(List<String[]> parseTransactions, String parseAccountNumber) {
        return OcrDTO.builder()
                .parseTransactions(parseTransactions)
                .parseAccountNumber(parseAccountNumber)
                .build();
    }
}
