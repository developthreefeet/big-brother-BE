package com.example.bigbrotherbe.domain.like.repository;

import com.example.bigbrotherbe.domain.like.entity.Key.LikeId;
import com.example.bigbrotherbe.domain.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, LikeId> {
}
