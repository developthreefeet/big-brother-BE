package com.example.bigbrotherbe.domain.faq.service;

import com.example.bigbrotherbe.domain.faq.dto.FAQModifyRequest;
import com.example.bigbrotherbe.domain.faq.dto.FAQRegisterRequest;
import com.example.bigbrotherbe.domain.notice.dto.NoticeModifyRequest;
import com.example.bigbrotherbe.domain.notice.dto.NoticeRegisterRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FAQService {
    public void register(FAQRegisterRequest faqRegisterRequest, List<MultipartFile> multipartFiles);

    public void modify(Long faqId, FAQModifyRequest faqModifyRequest, List<MultipartFile> multipartFiles);

    public void delete(Long faqId);
}
