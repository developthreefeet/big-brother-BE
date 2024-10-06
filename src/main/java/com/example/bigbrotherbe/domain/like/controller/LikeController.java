package com.example.bigbrotherbe.domain.like.controller;

import com.example.bigbrotherbe.domain.comment.dto.CommentRegisterRequest;
import com.example.bigbrotherbe.domain.like.dto.LikeDeleteRequest;
import com.example.bigbrotherbe.domain.like.dto.LikeRegisterRequest;
import com.example.bigbrotherbe.domain.like.service.LikeService;
import com.example.bigbrotherbe.global.exception.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.bigbrotherbe.global.exception.enums.SuccessCode.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/like")
public class LikeController {
    private final LikeService likeService;

    @PostMapping()
    public ResponseEntity<ApiResponse<Void>> registerComment(@RequestPart(value = "likeRegisterRequest") LikeRegisterRequest likeRegisterRequest){
        this.likeService.registerLike(likeRegisterRequest);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS));
    }

    @DeleteMapping()
    public ResponseEntity<ApiResponse<Void>> deleteComment(@RequestPart(value = "likeDeleteRequest") LikeDeleteRequest likeDeleteRequest) {
        this.likeService.deleteLike(likeDeleteRequest);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS));
    }
}
