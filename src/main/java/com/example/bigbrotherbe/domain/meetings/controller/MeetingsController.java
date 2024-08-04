package com.example.bigbrotherbe.domain.meetings.controller;

import com.example.bigbrotherbe.domain.meetings.dto.request.MeetingsRegisterRequest;
import com.example.bigbrotherbe.domain.meetings.dto.request.MeetingsUpdateRequest;
import com.example.bigbrotherbe.domain.meetings.dto.response.MeetingsResponse;
import com.example.bigbrotherbe.domain.meetings.entity.Meetings;
import com.example.bigbrotherbe.domain.meetings.service.MeetingsService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
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

    @GetMapping("/{meetingsId}")
    public ResponseEntity<MeetingsResponse> getMeetingsById(@PathVariable("meetingsId") Long MeetingsId) {
        MeetingsResponse meetingsResponse = meetingsService.getMeetingsById(MeetingsId);
        return ResponseEntity.ok().body(meetingsResponse);
    }

    @GetMapping("all/{affiliationId}")
    public ResponseEntity<Page<Meetings>> getMeetingsList(@PathVariable("affiliationId") Long affiliationId,
                                                          @RequestParam(name = "page", defaultValue = "0") int page,
                                                          @RequestParam(name = "size", defaultValue = "10") int size,
                                                          @RequestParam(name = "search", required = false) String search) {
        Page<Meetings> meetingsPage;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        if (search != null && !search.isEmpty()) {
            meetingsPage = meetingsService.searchMeetings(affiliationId, search, pageable);
        } else {
            meetingsPage = meetingsService.getMeetings(affiliationId, pageable);
        }
        return ResponseEntity.ok().body(meetingsPage);
    }
}
