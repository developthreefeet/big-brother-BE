package com.example.bigbrotherbe.domain.like.entity.Key;

import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.domain.notice.entity.Notice;
import com.example.bigbrotherbe.global.common.enums.EntityType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class LikeId implements Serializable {
    private Member member;
    private Long entityId;
    private EntityType entityType;
}
