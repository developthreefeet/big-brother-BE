package com.example.bigbrotherbe.domain.faq.controller;

import com.example.bigbrotherbe.domain.faq.dto.request.FAQUpdateRequest;
import com.example.bigbrotherbe.domain.faq.dto.request.FAQRegisterRequest;
import com.example.bigbrotherbe.domain.faq.dto.response.FAQResponse;
import com.example.bigbrotherbe.domain.faq.entity.FAQ;
import com.example.bigbrotherbe.domain.faq.service.FAQService;
import com.example.bigbrotherbe.global.common.constant.Constant;
import com.example.bigbrotherbe.global.common.exception.response.ApiResponse;
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

import static com.example.bigbrotherbe.global.common.exception.enums.SuccessCode.SUCCESS;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/faq")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
public class FAQAdminController {
    private final FAQService faqService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> registerFAQ(@RequestPart(value = "faqRegisterRequest") FAQRegisterRequest faqRegisterRequest,
                                                         @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
        faqService.register(faqRegisterRequest, multipartFiles);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS));
    }

    @PutMapping("/{faqId}")
    public ResponseEntity<ApiResponse<Void>> modifyFAQ(@PathVariable("faqId") Long faqId,
                                                       @RequestPart(value = "faqUpdateRequest") FAQUpdateRequest faqUpdateRequest,
                                                       @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
        faqService.modify(faqId, faqUpdateRequest, multipartFiles);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS));
    }

    @DeleteMapping("/{faqId}")
    public ResponseEntity<ApiResponse<Void>> deleteFAQ(@PathVariable("faqId") Long faqId) {
        faqService.delete(faqId);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS));
    }

    @GetMapping("/{faqId}")
    public ResponseEntity<ApiResponse<FAQResponse>> getFAQById(@PathVariable("faqId") Long faqId) {
        FAQResponse faqResponse = faqService.getFAQById(faqId);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, faqResponse));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<Page<FAQ>>> getFAQList(@RequestParam(name = "affiliation") String affiliation,
                                                             @RequestParam(name = "page", defaultValue = Constant.GetContent.PAGE_DEFAULT_VALUE) int page,
                                                             @RequestParam(name = "size", defaultValue = Constant.GetContent.SIZE_DEFAULT_VALUE) int size,
                                                             @RequestParam(name = "search", required = false) String search) {
        Page<FAQ> faqPage;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        if (search != null && !search.isEmpty()) {
            faqPage = faqService.searchFAQ(affiliation, search, pageable);
        } else {
            faqPage = faqService.getFAQ(affiliation, pageable);
        }
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, faqPage));
    }
}
