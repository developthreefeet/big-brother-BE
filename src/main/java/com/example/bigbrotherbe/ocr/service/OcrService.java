package com.example.bigbrotherbe.ocr.service;

import com.example.bigbrotherbe.ocr.dto.OcrDTO;
import org.springframework.web.multipart.MultipartFile;

public interface OcrService {
    OcrDTO extractText(MultipartFile multipartFile);
}
