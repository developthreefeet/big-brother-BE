package com.example.bigbrotherbe.global.ocr.service;

import com.example.bigbrotherbe.global.ocr.dto.OcrDto;
import org.springframework.web.multipart.MultipartFile;

public interface OcrService {
    OcrDto extractText(MultipartFile multipartFile);
}
