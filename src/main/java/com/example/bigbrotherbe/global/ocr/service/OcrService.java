package com.example.bigbrotherbe.global.ocr.service;

import com.example.bigbrotherbe.global.ocr.dto.OcrDTO;
import org.springframework.web.multipart.MultipartFile;

public interface OcrService {
    OcrDTO extractText(MultipartFile multipartFile);
}
