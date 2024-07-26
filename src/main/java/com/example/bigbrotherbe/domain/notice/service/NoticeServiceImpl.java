package com.example.bigbrotherbe.domain.notice.service;

import com.example.bigbrotherbe.domain.notice.dto.NoticeModifyRequest;
import com.example.bigbrotherbe.domain.notice.dto.NoticeRegisterRequest;
import com.example.bigbrotherbe.domain.notice.entity.Notice;
import com.example.bigbrotherbe.domain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
    private final NoticeRepository noticeRepository;
    @Override
    public void register(NoticeRegisterRequest noticeRegisterRequest) {
        noticeRepository.save(noticeRegisterRequest.toNoticeEntity());
    }

    @Override
    public void modify(Long noticeId, NoticeModifyRequest noticeModifyRequest) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoSuchElementException("do not exist"));

        notice.update(noticeModifyRequest.getTitle(), noticeModifyRequest.getContent());
        return;
    }

    @Override
    public void delete(Long noticeId) {
        return;
    }


}
