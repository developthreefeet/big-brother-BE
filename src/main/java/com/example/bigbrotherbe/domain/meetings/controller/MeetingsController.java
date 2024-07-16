package com.example.bigbrotherbe.domain.meetings.controller;

import com.example.bigbrotherbe.domain.meetings.dto.MeetingsRegisterRequest;
import com.example.bigbrotherbe.domain.meetings.service.MeetingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/big-brother/meeting")
public class MeetingsController {

    private final MeetingsService meetingsService;

    @PostMapping
    public ResponseEntity<Void> registerMeetings(@RequestBody MeetingsRegisterRequest meetingsRegisterRequest) {

        return ResponseEntity.ok().build();
    }

}
