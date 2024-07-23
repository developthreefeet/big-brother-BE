package com.example.bigbrotherbe.domain.member.controller;

import com.example.bigbrotherbe.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/big-brother")
@RequiredArgsConstructor
public class Controller {
    private final MemberService memberService;

    @GetMapping("/test")
    public String login() {
        return "test";
    }
}
