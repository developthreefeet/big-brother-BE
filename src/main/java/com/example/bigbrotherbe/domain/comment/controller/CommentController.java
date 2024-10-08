package com.example.bigbrotherbe.domain.comment.controller;

import com.example.bigbrotherbe.domain.comment.dto.CommentRegisterRequest;
import com.example.bigbrotherbe.domain.comment.dto.CommentReplyRequest;
import com.example.bigbrotherbe.domain.comment.dto.CommentUpdateRequest;
import com.example.bigbrotherbe.domain.comment.service.CommentService;
import com.example.bigbrotherbe.global.common.exception.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.bigbrotherbe.global.common.exception.enums.SuccessCode.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping()
    public ResponseEntity<ApiResponse<Void>> registerComment(@RequestBody CommentRegisterRequest commentRegisterRequest){
        this.commentService.registerComment(commentRegisterRequest);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS));
    }

//    @PostMapping("/reply")
//    public ResponseEntity<ApiResponse<Void>> registerReply(@RequestBody CommentReplyRequest commentReplyRequest){
//        this.commentService.registerReply(commentReplyRequest);
//        return ResponseEntity.ok(ApiResponse.success(SUCCESS));
//    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> updateComment(@PathVariable("commentId") Long id,
                                              @RequestBody CommentUpdateRequest commentUpdateRequest){
        this.commentService.updateComment(id, commentUpdateRequest);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable("commentId") Long id) {
        this.commentService.deleteComment(id);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS));
    }
}
