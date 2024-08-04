package com.example.bigbrotherbe.domain.faq.controller;

import com.example.bigbrotherbe.domain.faq.dto.FAQModifyRequest;
import com.example.bigbrotherbe.domain.faq.dto.FAQRegisterRequest;
import com.example.bigbrotherbe.domain.faq.service.FAQService;
import com.example.bigbrotherbe.domain.notice.dto.NoticeModifyRequest;
import com.example.bigbrotherbe.domain.notice.dto.NoticeRegisterRequest;
import com.example.bigbrotherbe.domain.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/big-brother/notice")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
public class FAQController {
    private final FAQService faqService;

    @PostMapping
    public ResponseEntity<Void> registerNotice(@RequestBody FAQRegisterRequest faqRegisterRequest,
                                               @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
        faqService.register(faqRegisterRequest, multipartFiles);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{faqId}")
    public ResponseEntity<Void> modifyNotice(@PathVariable("faqId") Long faqId, @RequestBody FAQModifyRequest faqModifyRequest){
        faqService.modify(faqId, faqModifyRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{faqId}")
    public ResponseEntity<Void> deleteNotice(@PathVariable("faqId") Long faqId){
        faqService.delete(faqId);
        return ResponseEntity.ok().build();
    }
}
