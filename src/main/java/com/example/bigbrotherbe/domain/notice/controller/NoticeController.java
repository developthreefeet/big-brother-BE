package com.example.bigbrotherbe.domain.notice.controller;

import com.example.bigbrotherbe.domain.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j                                          // 로깅 기능 추가
@RestController                                 // RESTful 웹서비스 컨트롤러
@RequestMapping("/members")                     // URL을 컨트롤러 클래스 또는 메서드와 매핑
@CrossOrigin(origins = "http://localhost:8080")   // 다른 도메인에서의 요청을 허용
@RequiredArgsConstructor                        // final 또는 @NonNull 어노테이션이 붙은 필드의 생성자를 자동으로 생성
public class NoticeController {
    private final NoticeService noticeService;
}
