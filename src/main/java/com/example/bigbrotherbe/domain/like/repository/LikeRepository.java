package com.example.bigbrotherbe.domain.like.repository;

import com.example.bigbrotherbe.domain.like.entity.Key.NoticeLikeId;
import com.example.bigbrotherbe.domain.like.entity.NoticeLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<NoticeLike, NoticeLikeId> {

}
