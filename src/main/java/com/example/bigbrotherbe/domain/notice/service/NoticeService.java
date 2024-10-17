package com.example.bigbrotherbe.domain.notice.service;

import com.example.bigbrotherbe.domain.notice.dto.request.NoticeUpdateRequest;
import com.example.bigbrotherbe.domain.notice.dto.request.NoticeRegisterRequest;
import com.example.bigbrotherbe.domain.notice.dto.response.NoticeResponse;
import com.example.bigbrotherbe.domain.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NoticeService {
    public void register(NoticeRegisterRequest noticeRegisterRequest, List<MultipartFile> multipartFiles);

    public void modify(Long noticeId, NoticeUpdateRequest noticeUpdateRequest, List<MultipartFile> multipartFiles);

    public void delete(Long noticeId);

    NoticeResponse getNoticeById(Long noticeId);

    Page<Notice> getNotice(String affiliation, Pageable pageable);

    Page<Notice> searchNotice(String affiliation, String title, Pageable pageable);
}
