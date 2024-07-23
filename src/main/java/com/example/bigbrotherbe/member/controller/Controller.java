package com.example.bigbrotherbe.member.controller;

import com.example.bigbrotherbe.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/big-brother")
@RequiredArgsConstructor
public class Controller {
    private final MemberService memberService;
    @GetMapping("/test")
    public String login(){
        return "test";
    }
}
