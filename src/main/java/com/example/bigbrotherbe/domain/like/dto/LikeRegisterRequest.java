package com.example.bigbrotherbe.domain.like.dto;

import com.example.bigbrotherbe.domain.like.entity.NoticeLike;
import com.example.bigbrotherbe.domain.member.entity.Member;
import lombok.Getter;

@Getter
public class LikeRegisterRequest {
    private String entityType;
    private Long entityId;

    public NoticeLike toLikeEntity(Member member){
        return NoticeLike.builder()
                .member(member)
                .build();
    }
}
