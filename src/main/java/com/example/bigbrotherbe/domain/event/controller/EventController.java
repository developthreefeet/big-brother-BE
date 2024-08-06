package com.example.bigbrotherbe.domain.event.controller;

import com.example.bigbrotherbe.domain.event.dto.request.EventRegisterRequest;
import com.example.bigbrotherbe.domain.event.dto.request.EventUpdateRequest;
import com.example.bigbrotherbe.domain.event.service.EventService;
import com.example.bigbrotherbe.domain.meetings.dto.request.MeetingsRegisterRequest;
import com.example.bigbrotherbe.domain.meetings.dto.request.MeetingsUpdateRequest;
import com.example.bigbrotherbe.global.exception.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.bigbrotherbe.global.exception.enums.SuccessCode.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/big-brother/event")
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> registerEvent(@RequestPart(value = "eventRegisterRequest") EventRegisterRequest eventRegisterRequest,
                                                           @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
        eventService.registerEvent(eventRegisterRequest, multipartFiles);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<ApiResponse<Void>> updateEvent(@PathVariable("eventId") Long eventId,
                                                         @RequestPart(value = "eventUpdateRequest") EventUpdateRequest eventUpdateRequest,
                                                         @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
        eventService.updateEvent(eventId, eventUpdateRequest, multipartFiles);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(@PathVariable("eventId") Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS));
    }
}
