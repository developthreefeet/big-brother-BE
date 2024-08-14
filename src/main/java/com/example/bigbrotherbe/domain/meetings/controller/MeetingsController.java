package com.example.bigbrotherbe.domain.meetings.controller;

import com.example.bigbrotherbe.domain.meetings.dto.response.MeetingsResponse;
import com.example.bigbrotherbe.domain.meetings.entity.Meetings;
import com.example.bigbrotherbe.domain.meetings.service.MeetingsService;

import com.example.bigbrotherbe.global.exception.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static com.example.bigbrotherbe.global.constant.Constant.GetContent.PAGE_DEFAULT_VALUE;
import static com.example.bigbrotherbe.global.constant.Constant.GetContent.SIZE_DEFAULT_VALUE;
import static com.example.bigbrotherbe.global.exception.enums.SuccessCode.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/meetings")
public class MeetingsController {

    private final MeetingsService meetingsService;

    @GetMapping("/{meetingsId}")
    public ResponseEntity<ApiResponse<MeetingsResponse>> getMeetingsById(@PathVariable("meetingsId") Long MeetingsId) {
        MeetingsResponse meetingsResponse = meetingsService.getMeetingsById(MeetingsId);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, meetingsResponse));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<Page<Meetings>>> getMeetingsList(@RequestParam(name = "affiliationId") Long affiliationId,
                                                                       @RequestParam(name = "page", defaultValue = PAGE_DEFAULT_VALUE) int page,
                                                                       @RequestParam(name = "size", defaultValue = SIZE_DEFAULT_VALUE) int size,
                                                                       @RequestParam(name = "search", required = false) String search) {
        Page<Meetings> meetingsPage;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        if (search != null && !search.isEmpty()) {
            meetingsPage = meetingsService.searchMeetings(affiliationId, search, pageable);
        } else {
            meetingsPage = meetingsService.getMeetings(affiliationId, pageable);
        }
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, meetingsPage));
    }
}
