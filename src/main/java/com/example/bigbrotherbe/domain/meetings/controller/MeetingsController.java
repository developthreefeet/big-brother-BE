package com.example.bigbrotherbe.domain.meetings.controller;

import com.example.bigbrotherbe.domain.meetings.dto.MeetingsRegisterRequest;
import com.example.bigbrotherbe.domain.meetings.dto.MeetingsUpdateRequest;
import com.example.bigbrotherbe.domain.meetings.service.MeetingsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/big-brother/meetings")
public class MeetingsController {

    private final MeetingsServiceImpl meetingsServiceImpl;

    @PostMapping
    public ResponseEntity<Void> registerMeetings(@RequestPart(value = "meetingsRegisterRequest") MeetingsRegisterRequest meetingsRegisterRequest,
                                                 @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
        meetingsServiceImpl.registerMeetings(meetingsRegisterRequest, multipartFiles);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{meetingsId}")
    public ResponseEntity<Void> updateMeetings(@PathVariable("meetingsId") Long meetingsId, @RequestBody MeetingsUpdateRequest meetingsUpdateRequest) {
        meetingsServiceImpl.updateMeetings(meetingsId, meetingsUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{meetingsId}")
    public ResponseEntity<Void> deleteMeetings(@PathVariable("meetingsId") Long meetingsId) {

        return ResponseEntity.ok().build();
    }
}
