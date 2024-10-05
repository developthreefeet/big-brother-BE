package com.example.bigbrotherbe.domain.notice.controller;

import com.example.bigbrotherbe.domain.notice.dto.response.NoticeResponse;
import com.example.bigbrotherbe.domain.notice.entity.Notice;
import com.example.bigbrotherbe.domain.notice.service.NoticeService;
import com.example.bigbrotherbe.global.common.constant.Constant;
import com.example.bigbrotherbe.global.common.exception.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.bigbrotherbe.global.common.exception.enums.SuccessCode.SUCCESS;

@Slf4j                                          // 로깅 기능 추가
@RestController                                 // RESTful 웹서비스 컨트롤러
@RequestMapping("/api/v1/notice")            // URL을 컨트롤러 클래스 또는 메서드와 매핑
@CrossOrigin(origins = "http://localhost:8080")   // 다른 도메인에서의 요청을 허용
@RequiredArgsConstructor                        // final 또는 @NonNull 어노테이션이 붙은 필드의 생성자를 자동으로 생성
public class NoticeController {
    private final NoticeService noticeService;

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
