package com.example.bigbrotherbe.domain.event.controller;

import com.example.bigbrotherbe.domain.event.dto.request.EventRegisterRequest;
import com.example.bigbrotherbe.domain.event.service.EventService;
import com.example.bigbrotherbe.domain.meetings.dto.request.MeetingsRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/big-brother/event")
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<Void> registerEvent(@RequestPart(value = "eventRegisterRequest") EventRegisterRequest eventRegisterRequest,
                                              @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
        eventService.registerEvent(eventRegisterRequest, multipartFiles);
        return ResponseEntity.ok().build();
    }
}
