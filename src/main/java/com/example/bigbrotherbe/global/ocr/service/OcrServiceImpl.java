package com.example.bigbrotherbe.global.ocr.service;

import com.example.bigbrotherbe.global.ocr.dto.OcrDto;
import com.example.bigbrotherbe.global.ocr.util.OcrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OcrServiceImpl implements OcrService {

    private final OcrUtil ocrUtil;

    @Override
    public OcrDto extractText(MultipartFile multipartFile) {
        // ocr로 모든 text 추출한 거
        String extractedText = ocrUtil.extractTextFromPDF(multipartFile);

        List<String[]> parseTransactions = ocrUtil.parseTransactions(extractedText);
        String parseAccountNumber = ocrUtil.parseAccountNumber(extractedText);

        // parsing 가공해서 응답
        return OcrDto.fromOcrResponse(parseTransactions, parseAccountNumber);
    }
}
