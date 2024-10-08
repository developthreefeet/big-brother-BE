package com.example.bigbrotherbe.domain.like.service;

import com.example.bigbrotherbe.domain.comment.enums.EntityType;
import com.example.bigbrotherbe.domain.like.dto.LikeDeleteRequest;
import com.example.bigbrotherbe.domain.like.dto.LikeRegisterRequest;
import com.example.bigbrotherbe.domain.like.entity.Key.NoticeLikeId;
import com.example.bigbrotherbe.domain.like.entity.NoticeLike;
import com.example.bigbrotherbe.domain.like.repository.LikeRepository;
import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.domain.notice.entity.Notice;
import com.example.bigbrotherbe.domain.notice.repository.NoticeRepository;

import com.example.bigbrotherbe.global.auth.util.AuthUtil;
import com.example.bigbrotherbe.global.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.bigbrotherbe.global.common.exception.enums.ErrorCode.NOT_FOUNT_ENTITY;


@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService{
    private final LikeRepository likeRepository;

    private final NoticeRepository noticeRepository;

    private final AuthUtil authUtil;

    @Override
    public void registerLike(LikeRegisterRequest likeRegisterRequest) {
        Member member = authUtil.getLoginMember();

        String entityType = likeRegisterRequest.getEntityType();
        Long entityId = likeRegisterRequest.getEntityId();

        if (entityType.equals(EntityType.NOTICE.getType())){
            NoticeLike noticeLike = likeRegisterRequest.toLikeEntity(member);

            noticeLike.linkNotice(noticeRepository.findById(entityId)
                    .orElseThrow(() -> new BusinessException(NOT_FOUNT_ENTITY)));
            this.likeRepository.save(noticeLike);

        }else if(entityType.equals(EntityType.EVENT.getType())){
//            like.linkEvent(this.eventRepository.findById(entityId)
//                    .orElseThrow(() -> new BusinessException(NOT_FOUNT_ENTITY)));
        }else {
            throw new BusinessException(NOT_FOUNT_ENTITY);
        }
    }

    @Override
    public void deleteLike(LikeDeleteRequest likeDeleteRequest) {
        Member member = authUtil.getLoginMember();
        Notice notice = this.noticeRepository.findById(likeDeleteRequest.getEntityId())
                .orElseThrow(() -> new BusinessException(NOT_FOUNT_ENTITY));

        NoticeLike noticeLike = this.likeRepository.getReferenceById(new NoticeLikeId(member, notice));

        this.likeRepository.delete(noticeLike);
    }

}
