package com.example.bigbrotherbe.domain.notice.controller;

import com.example.bigbrotherbe.domain.notice.dto.request.NoticeModifyRequest;
import com.example.bigbrotherbe.domain.notice.dto.request.NoticeRegisterRequest;
import com.example.bigbrotherbe.domain.notice.dto.response.NoticeResponse;
import com.example.bigbrotherbe.domain.notice.entity.Notice;
import com.example.bigbrotherbe.domain.notice.service.NoticeService;
import com.example.bigbrotherbe.global.constant.Constant;
import com.example.bigbrotherbe.global.exception.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.bigbrotherbe.global.exception.enums.SuccessCode.SUCCESS;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/notice")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
public class NoticeAdminController {
    private final NoticeService noticeService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> registerNotice(@RequestPart(value = "noticeRegisterRequest") NoticeRegisterRequest noticeRegisterRequest,
                                                            @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
        noticeService.register(noticeRegisterRequest, multipartFiles);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS));
    }

    @PutMapping("/{noticeId}")
    public ResponseEntity<ApiResponse<Void>> modifyNotice(@PathVariable("noticeId") Long noticeId,
                                                          @RequestPart(value = "noticeModifyRequest") NoticeModifyRequest noticeModifyRequest,
                                                          @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
        noticeService.modify(noticeId, noticeModifyRequest, multipartFiles);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS));
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<ApiResponse<Void>> deleteNotice(@PathVariable("noticeId") Long noticeId) {
        noticeService.delete(noticeId);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS));
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<ApiResponse<NoticeResponse>> getNoticeById(@PathVariable("noticeId") Long noticeId) {
        NoticeResponse noticeResponse = noticeService.getNoticeById(noticeId);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, noticeResponse));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<Page<Notice>>> getNoticeList(@RequestParam(name = "affiliation") String affiliation,
                                                                   @RequestParam(name = "page", defaultValue = Constant.GetContent.PAGE_DEFAULT_VALUE) int page,
                                                                   @RequestParam(name = "size", defaultValue = Constant.GetContent.SIZE_DEFAULT_VALUE) int size,
                                                                   @RequestParam(name = "search", required = false) String search) {
        Page<Notice> noticePage;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        if (search != null && !search.isEmpty()) {
            noticePage = noticeService.searchNotice(affiliation, search, pageable);
        } else {
            noticePage = noticeService.getNotice(affiliation, pageable);
        }
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, noticePage));
    }
}
