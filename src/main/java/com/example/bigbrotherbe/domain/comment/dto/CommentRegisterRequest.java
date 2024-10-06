package com.example.bigbrotherbe.domain.comment.dto;

import com.example.bigbrotherbe.domain.comment.entity.Comment;
import com.example.bigbrotherbe.domain.member.entity.Member;
import lombok.Getter;

@Getter
public class CommentRegisterRequest {
    private String content;
    private String entityType;
    private Long entityId;

    public Comment toCommentEntity(Member member){
        return Comment.builder()
                .content(this.content)
                .members(member)
                .build();
    }
}
