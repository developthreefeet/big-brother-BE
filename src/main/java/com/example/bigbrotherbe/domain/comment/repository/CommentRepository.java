package com.example.bigbrotherbe.domain.comment.repository;

import com.example.bigbrotherbe.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
