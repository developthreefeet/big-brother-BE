package com.example.bigbrotherbe.domain.like.dto;

import com.example.bigbrotherbe.domain.like.entity.Like;
import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.global.common.enums.EntityType;
import lombok.Getter;

@Getter
public class LikeRegisterRequest {
    private String entityType;
    private Long entityId;

    public Like toLikeEntity(Member member){
        return Like.builder()
                .member(member)
                .entityType(EntityType.getEntityType(this.entityType))
                .entityId(this.entityId)
                .build();
    }
}
