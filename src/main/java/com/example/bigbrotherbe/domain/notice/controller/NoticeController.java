package com.example.bigbrotherbe.domain.notice.controller;

import com.example.bigbrotherbe.domain.notice.dto.NoticeModifyRequest;
import com.example.bigbrotherbe.domain.notice.dto.NoticeRegisterRequest;
import com.example.bigbrotherbe.domain.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j                                          // 로깅 기능 추가
@RestController                                 // RESTful 웹서비스 컨트롤러
@RequestMapping("/api/big-brother/notice")    // URL을 컨트롤러 클래스 또는 메서드와 매핑
@CrossOrigin(origins = "http://localhost:8080")   // 다른 도메인에서의 요청을 허용
@RequiredArgsConstructor                        // final 또는 @NonNull 어노테이션이 붙은 필드의 생성자를 자동으로 생성
public class NoticeController {
    private final NoticeService noticeService;

    @PostMapping
    public ResponseEntity<Void> registerNotice(@RequestBody NoticeRegisterRequest noticeRegisterRequest){
        noticeService.register(noticeRegisterRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{noticeId}")
    public ResponseEntity<Void> modifyNotice(@PathVariable("noticeId") Long noticeId, NoticeModifyRequest noticeModifyRequest){
        noticeService.modify(noticeId, noticeModifyRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{noticeId}")
    public  ResponseEntity<Void> deleteNotice(@PathVariable("noticeId") Long noticeId){
        noticeService.delete(noticeId);
        return ResponseEntity.ok().build();
    }
}
