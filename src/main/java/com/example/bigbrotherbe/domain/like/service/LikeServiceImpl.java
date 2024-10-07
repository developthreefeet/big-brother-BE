package com.example.bigbrotherbe.domain.like.service;

import com.example.bigbrotherbe.domain.campusNotice.repository.CampusNoticeRepository;
import com.example.bigbrotherbe.domain.event.repository.EventRepository;
import com.example.bigbrotherbe.domain.faq.repository.FAQRepository;
import com.example.bigbrotherbe.domain.like.dto.LikeDeleteRequest;
import com.example.bigbrotherbe.domain.like.dto.LikeRegisterRequest;
import com.example.bigbrotherbe.domain.like.entity.Key.LikeId;
import com.example.bigbrotherbe.domain.like.entity.Like;
import com.example.bigbrotherbe.domain.like.repository.LikeRepository;
import com.example.bigbrotherbe.domain.meetings.repository.MeetingsRepository;
import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.domain.notice.repository.NoticeRepository;
import com.example.bigbrotherbe.domain.rule.repository.RuleRepository;
import com.example.bigbrotherbe.global.auth.util.AuthUtil;
import com.example.bigbrotherbe.global.common.enums.EntityType;
import com.example.bigbrotherbe.global.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.bigbrotherbe.global.common.exception.enums.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService{
    private final LikeRepository likeRepository;

    private final AuthUtil authUtil;
    @Override
    public void registerLike(LikeRegisterRequest likeRegisterRequest) {
        Member member = authUtil.getLoginMember();

        String entityType = likeRegisterRequest.getEntityType();
        Long entityId = likeRegisterRequest.getEntityId();
        if (this.likeRepository.existsById(new LikeId(member, entityId, EntityType.getEntityType(entityType)))){
            throw new BusinessException(ALREADY_EXIST_LIKE);
        }

        Like like = likeRegisterRequest.toLikeEntity(member);
        this.likeRepository.save(like);
    }

    @Override
    public void deleteLike(LikeDeleteRequest likeDeleteRequest) {
        Member member = authUtil.getLoginMember();

        EntityType entityType = EntityType.getEntityType(likeDeleteRequest.getEntityType());
        Optional<Like> like = this.likeRepository.findById(new LikeId(member, likeDeleteRequest.getEntityId(), entityType));
        if (like.isEmpty()) {
            throw new BusinessException(NO_EXIST_LIKE);
        }

        this.likeRepository.delete(like.get());
    }

}
