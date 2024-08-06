package com.example.bigbrotherbe.domain.faq.service;

import com.example.bigbrotherbe.domain.faq.dto.request.FAQModifyRequest;
import com.example.bigbrotherbe.domain.faq.dto.request.FAQRegisterRequest;
import com.example.bigbrotherbe.domain.faq.dto.response.FAQResponse;
import com.example.bigbrotherbe.domain.faq.entity.FAQ;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FAQService {
    public void register(FAQRegisterRequest faqRegisterRequest, List<MultipartFile> multipartFiles);

    public void modify(Long faqId, FAQModifyRequest faqModifyRequest, List<MultipartFile> multipartFiles);

    public void delete(Long faqId);

    FAQResponse getFAQById(Long faqId);

    Page<FAQ> getFAQ(Long affiliationId, Pageable pageable);

    Page<FAQ> searchFAQ(Long affiliationId, String title, Pageable pageable);
}
