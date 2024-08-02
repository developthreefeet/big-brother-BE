package com.example.bigbrotherbe.domain.notice.service;

import com.example.bigbrotherbe.domain.notice.dto.NoticeModifyRequest;
import com.example.bigbrotherbe.domain.notice.dto.NoticeRegisterRequest;
import com.example.bigbrotherbe.domain.notice.entity.Notice;
import com.example.bigbrotherbe.domain.notice.repository.NoticeRepository;
import com.example.bigbrotherbe.global.exception.BusinessException;
import com.example.bigbrotherbe.global.exception.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)   // 트랜잭션 시작 및 커밋 -> 모든 예외상황 발생시 롤백
    public void register(NoticeRegisterRequest noticeRegisterRequest) {
        noticeRepository.save(noticeRegisterRequest.toNoticeEntity());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(Long noticeId, NoticeModifyRequest noticeModifyRequest) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_EXIST_NOTICE));

        notice.update(noticeModifyRequest.getTitle(), noticeModifyRequest.getContent());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_EXIST_NOTICE));
//        Member member = authUtil.getLoginMember();
//
//        if (participantService.findParticipantInfo(member, notice.getMeeting()).getRole() != Role.HOST) {
//            throw new BusinessException(NOT_HOST_OF_MEETING);
//        }

        noticeRepository.delete(notice);
    }


}
