package com.example.bigbrotherbe.domain.like.dto;

import lombok.Getter;

@Getter
public class LikeDeleteRequest {
    private String entityType;
    private Long entityId;
}
