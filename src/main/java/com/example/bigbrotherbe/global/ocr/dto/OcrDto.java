package com.example.bigbrotherbe.global.ocr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OcrDto {
    private List<String[]> parseTransactions;
    private String parseAccountNumber;

    public static OcrDto fromOcrResponse(List<String[]> parseTransactions, String parseAccountNumber) {
        return OcrDto.builder()
                .parseTransactions(parseTransactions)
                .parseAccountNumber(parseAccountNumber)
                .build();
    }
}
