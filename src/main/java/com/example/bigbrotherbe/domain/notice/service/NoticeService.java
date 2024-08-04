package com.example.bigbrotherbe.domain.notice.service;

import com.example.bigbrotherbe.domain.notice.dto.NoticeModifyRequest;
import com.example.bigbrotherbe.domain.notice.dto.NoticeRegisterRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NoticeService {
    public void register(NoticeRegisterRequest noticeRegisterRequest, List<MultipartFile> multipartFiles);

    public void modify(Long noticeId, NoticeModifyRequest noticeModifyRequest, List<MultipartFile> multipartFiles);

    public void delete(Long noticeId);
}
