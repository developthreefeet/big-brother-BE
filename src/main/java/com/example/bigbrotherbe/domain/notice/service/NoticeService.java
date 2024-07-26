package com.example.bigbrotherbe.domain.notice.service;

import com.example.bigbrotherbe.domain.notice.dto.NoticeModifyRequest;
import com.example.bigbrotherbe.domain.notice.dto.NoticeRegisterRequest;

public interface NoticeService {
    public void register(NoticeRegisterRequest noticeRegisterRequest);

    public void modify(Long noticeId, NoticeModifyRequest noticeModifyRequest);

    public void delete(Long noticeId);
}
