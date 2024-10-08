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
import com.example.bigbrotherbe.global.common.constant.Constant;
import com.example.bigbrotherbe.global.common.enums.EntityType;
import com.example.bigbrotherbe.global.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.bigbrotherbe.global.common.exception.enums.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;

    private final EventRepository eventRepository;
    private final NoticeRepository noticeRepository;

    private final AuthUtil authUtil;
    @Override
    public void registerComment(CommentRegisterRequest commentRegisterRequest) {
        Member member = authUtil.getLoginMember();
        Comment comment;
        if (commentRegisterRequest.getParentId() != null){
            Comment parentComment = commentRepository.findById(commentRegisterRequest.getParentId())
                    .orElseThrow(() -> new BusinessException(NO_EXIST_COMMENT));
            if (parentComment.isDeleted()){
                throw new BusinessException(NO_EXIST_COMMENT);
            }
            comment = commentRegisterRequest.toCommentEntity(member, parentComment);
            this.setCommentEntity(comment, parentComment);
        }else{
            comment = commentRegisterRequest.toCommentEntity(member);
            this.setCommentEntity(comment, commentRegisterRequest.getEntityType(), commentRegisterRequest.getEntityId());
        }

        commentRepository.save(comment);
    }

    @Override
    public void updateComment(Long id, CommentUpdateRequest commentUpdateRequest) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(NO_EXIST_COMMENT));

        if (!comment.getMembers().equals(authUtil.getLoginMember())){
            throw new BusinessException(NOT_REGISTER_MEMBER);
        }
        if (comment.isDeleted()){
            throw new BusinessException(NO_EXIST_COMMENT);
        }
        comment.update(commentUpdateRequest.getContent());
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(NO_EXIST_COMMENT));

        if (!comment.getMembers().equals(authUtil.getLoginMember())){
            throw new BusinessException(NOT_REGISTER_MEMBER);
        }
        if (comment.isDeleted()){
            throw new BusinessException(NO_EXIST_COMMENT);
        }
        commentRepository.delete(comment);
    }

    private void setCommentEntity(Comment comment, String entityType, Long entityId){
        /*
        comment와 관계를 맺은 entity 연결
         */
        EntityType type;
        switch (entityType){
            case Constant.Entity.NOTICE:
                comment.linkNotice(noticeRepository.findById(entityId)
                        .orElseThrow(() -> new BusinessException(NOT_FOUNT_ENTITY)));
                type = EntityType.NOTICE_TYPE;
                break;
            case Constant.Entity.EVENT:
                comment.linkEvent(this.eventRepository.findById(entityId)
                        .orElseThrow(() -> new BusinessException(NOT_FOUNT_ENTITY)));
                type = EntityType.EVENT_TYPE;
                break;
            default:
                throw new BusinessException(NOT_FOUNT_ENTITY);
        }
        this.setCommentEntityType(comment, type);
    }

    private void setCommentEntity(Comment comment, Comment parent){
        /*
        부모 comment와 관계를 맺은 entity 연결
         */
        EntityType type = EntityType.getEntityType(parent.getEntityType().getType());
        switch (parent.getEntityType().getType()){
            case Constant.Entity.NOTICE:
                comment.linkNotice(parent.getNotice());
                break;
            case Constant.Entity.EVENT:
                comment.linkEvent(parent.getEvent());
                break;
            default:
                throw new BusinessException(NOT_FOUNT_ENTITY);
        }
        this.setCommentEntityType(comment, type);
    }

    private void setCommentEntityType(Comment comment, EntityType entityType){
        comment.setEntityType(entityType);
    }
}
