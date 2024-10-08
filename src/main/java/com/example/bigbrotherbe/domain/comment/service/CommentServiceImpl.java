package com.example.bigbrotherbe.domain.comment.service;

import com.example.bigbrotherbe.domain.comment.dto.CommentRegisterRequest;
import com.example.bigbrotherbe.domain.comment.dto.CommentReplyRequest;
import com.example.bigbrotherbe.domain.comment.dto.CommentUpdateRequest;
import com.example.bigbrotherbe.domain.comment.entity.Comment;
import com.example.bigbrotherbe.domain.comment.enums.EntityType;
import com.example.bigbrotherbe.domain.comment.repository.CommentRepository;
import com.example.bigbrotherbe.domain.event.repository.EventRepository;
import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.domain.notice.repository.NoticeRepository;

import com.example.bigbrotherbe.global.auth.util.AuthUtil;
import com.example.bigbrotherbe.global.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.bigbrotherbe.global.common.exception.enums.ErrorCode.NOT_FOUNT_ENTITY;
import static com.example.bigbrotherbe.global.common.exception.enums.ErrorCode.NO_EXIST_COMMENT;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final NoticeRepository noticeRepository;

    private final AuthUtil authUtil;

    @Override
    public void registerComment(CommentRegisterRequest commentRegisterRequest) {
        Member member = authUtil.getLoginMember();

        Comment comment = commentRegisterRequest.toCommentEntity(member);
        this.setCommentEntity(comment, commentRegisterRequest.getEntityType(), commentRegisterRequest.getEntityId());

        commentRepository.save(comment);
    }

    @Override
    public void registerReply(CommentReplyRequest commentReplyRequest) {
        Member member = authUtil.getLoginMember();
        Comment parentComment = commentRepository.findById(commentReplyRequest.getParentId())
                .orElseThrow(() -> new BusinessException(NO_EXIST_COMMENT));

        Comment comment = commentReplyRequest.toCommentEntity(member, parentComment);
        this.setCommentEntity(comment, parentComment);

        commentRepository.save(comment);
    }

    @Override
    public void updateComment(Long id, CommentUpdateRequest commentUpdateRequest) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(NO_EXIST_COMMENT));
        comment.update(commentUpdateRequest.getContent());
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(NO_EXIST_COMMENT));

        commentRepository.delete(comment);
    }

    private void setCommentEntity(Comment comment, String entityType, Long entityId) {
        /*
        comment와 관계를 맺은 entity 연결
         */
        if (entityType.equals(EntityType.NOTICE.getType())) {
            comment.linkNotice(noticeRepository.findById(entityId)
                    .orElseThrow(() -> new BusinessException(NOT_FOUNT_ENTITY)));
        } else if (entityType.equals(EntityType.EVENT.getType())) {
            comment.linkEvent(this.eventRepository.findById(entityId)
                    .orElseThrow(() -> new BusinessException(NOT_FOUNT_ENTITY)));
        } else {
            throw new BusinessException(NOT_FOUNT_ENTITY);
        }
    }

    private void setCommentEntity(Comment comment, Comment parent) {
        /*
        부모 comment외 관계를 맺은 entity 연결
         */
        if (parent.getNotice() != null) {
            comment.linkNotice(parent.getNotice());
        } else if (parent.getEvent() != null) {
            comment.linkEvent(parent.getEvent());
        } else {
            throw new BusinessException(NOT_FOUNT_ENTITY);
        }
    }
}
