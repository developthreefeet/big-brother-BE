package com.example.bigbrotherbe.domain.faq.controller;

import com.example.bigbrotherbe.domain.faq.dto.FAQModifyRequest;
import com.example.bigbrotherbe.domain.faq.dto.FAQRegisterRequest;
import com.example.bigbrotherbe.domain.faq.service.FAQService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/big-brother/faq")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
public class FAQController {
    private final FAQService faqService;

    @PostMapping
    public ResponseEntity<Void> registerFAQ(@RequestBody FAQRegisterRequest faqRegisterRequest,
                                               @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
        faqService.register(faqRegisterRequest, multipartFiles);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{faqId}")
    public ResponseEntity<Void> modifyFAQ(@PathVariable("faqId") Long faqId, @RequestBody FAQModifyRequest faqModifyRequest,
                                          @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles){
        faqService.modify(faqId, faqModifyRequest, multipartFiles);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{faqId}")
    public ResponseEntity<Void> deleteFAQ(@PathVariable("faqId") Long faqId){
        faqService.delete(faqId);
        return ResponseEntity.ok().build();
    }
}
