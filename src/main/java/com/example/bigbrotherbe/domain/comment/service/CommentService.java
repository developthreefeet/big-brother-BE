package com.example.bigbrotherbe.domain.comment.service;

import com.example.bigbrotherbe.domain.comment.dto.CommentRegisterRequest;
import com.example.bigbrotherbe.domain.comment.dto.CommentReplyRequest;
import com.example.bigbrotherbe.domain.comment.dto.CommentUpdateRequest;

public interface CommentService {
    public void registerComment(CommentRegisterRequest commentRegisterRequest);
    public void updateComment(Long id, CommentUpdateRequest commentUpdateRequest);
    public void deleteComment(Long id);
}
