package com.example.bigbrotherbe.domain.faq.controller;

import com.example.bigbrotherbe.domain.faq.dto.request.FAQModifyRequest;
import com.example.bigbrotherbe.domain.faq.dto.request.FAQRegisterRequest;
import com.example.bigbrotherbe.domain.faq.dto.response.FAQResponse;
import com.example.bigbrotherbe.domain.faq.entity.FAQ;
import com.example.bigbrotherbe.domain.faq.service.FAQService;
import com.example.bigbrotherbe.global.constant.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/faq")
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
                                          @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
        faqService.modify(faqId, faqModifyRequest, multipartFiles);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{faqId}")
    public ResponseEntity<Void> deleteFAQ(@PathVariable("faqId") Long faqId) {
        faqService.delete(faqId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{faqId}")
    public ResponseEntity<FAQResponse> getFAQById(@PathVariable("faqId") Long faqId) {
        FAQResponse faqResponse = faqService.getFAQById(faqId);
        return ResponseEntity.ok().body(faqResponse);
    }

    @GetMapping()
    public ResponseEntity<Page<FAQ>> getFAQList(@RequestParam(name = "affiliationId") Long affiliationId,
                                                @RequestParam(name = "page", defaultValue = Constant.GetContent.PAGE_DEFAULT_VALUE) int page,
                                                @RequestParam(name = "size", defaultValue = Constant.GetContent.SIZE_DEFAULT_VALUE) int size,
                                                @RequestParam(name = "search", required = false) String search) {
        Page<FAQ> faqPage;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        if (search != null && !search.isEmpty()) {
            faqPage = faqService.searchFAQ(affiliationId, search, pageable);
        } else {
            faqPage = faqService.getFAQ(affiliationId, pageable);
        }
        return ResponseEntity.ok().body(faqPage);
    }
}
