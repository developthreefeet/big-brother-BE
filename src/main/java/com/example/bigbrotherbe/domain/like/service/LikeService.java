package com.example.bigbrotherbe.domain.like.service;

import com.example.bigbrotherbe.domain.like.dto.LikeDeleteRequest;
import com.example.bigbrotherbe.domain.like.dto.LikeRegisterRequest;

public interface LikeService {
    public void registerLike(LikeRegisterRequest likeRegisterRequest);
    public void deleteLike(LikeDeleteRequest likeDeleteRequest);
}
