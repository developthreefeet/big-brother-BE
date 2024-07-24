package com.example.bigbrotherbe.notice.service;

import com.example.bigbrotherbe.notice.entry.dto.NoticeDto;

public interface NoticeService {
    public boolean register(NoticeDto noticeDto);

    public boolean modify(NoticeDto noticeDto);
}
