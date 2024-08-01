package com.example.bigbrotherbe.domain.meetings.controller;

import com.example.bigbrotherbe.domain.meetings.dto.MeetingsRegisterRequest;
import com.example.bigbrotherbe.domain.meetings.dto.MeetingsUpdateRequest;
import com.example.bigbrotherbe.domain.meetings.service.MeetingsService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/big-brother/meetings")
public class MeetingsController {

    private final MeetingsService meetingsService;

    @PostMapping
    public ResponseEntity<Void> registerMeetings(@RequestPart(value = "meetingsRegisterRequest") MeetingsRegisterRequest meetingsRegisterRequest,
                                                 @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
        meetingsService.registerMeetings(meetingsRegisterRequest, multipartFiles);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{meetingsId}")
    public ResponseEntity<Void> updateMeetings(@PathVariable("meetingsId") Long meetingsId,
                                               @RequestPart(value = "meetingsUpdateRequest") MeetingsUpdateRequest meetingsUpdateRequest,
                                               @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
        meetingsService.updateMeetings(meetingsId, meetingsUpdateRequest, multipartFiles);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{meetingsId}")
    public ResponseEntity<Void> deleteMeetings(@PathVariable("meetingsId") Long meetingsId) {
        meetingsService.deleteMeetings(meetingsId);
        return ResponseEntity.ok().build();
    }
}
