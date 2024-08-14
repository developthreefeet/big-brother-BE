package com.example.bigbrotherbe.domain.event.controller;

import com.example.bigbrotherbe.domain.event.dto.request.EventRegisterRequest;
import com.example.bigbrotherbe.domain.event.dto.request.EventUpdateRequest;
import com.example.bigbrotherbe.domain.event.dto.response.EventResponse;
import com.example.bigbrotherbe.domain.event.entity.Event;
import com.example.bigbrotherbe.domain.event.service.EventService;
import com.example.bigbrotherbe.global.exception.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.bigbrotherbe.global.constant.Constant.GetContent.PAGE_DEFAULT_VALUE;
import static com.example.bigbrotherbe.global.constant.Constant.GetContent.SIZE_DEFAULT_VALUE;
import static com.example.bigbrotherbe.global.exception.enums.SuccessCode.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/event")
public class EventAdminController {

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

    @GetMapping("/{eventId}")
    public ResponseEntity<ApiResponse<EventResponse>> getEventById(@PathVariable("eventId") Long eventId) {
        EventResponse eventResponse = eventService.getEventById(eventId);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, eventResponse));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<Page<Event>>> getMeetingsList(@RequestParam(name = "affiliation") String affiliation,
                                                                    @RequestParam(name = "page", defaultValue = PAGE_DEFAULT_VALUE) int page,
                                                                    @RequestParam(name = "size", defaultValue = SIZE_DEFAULT_VALUE) int size,
                                                                    @RequestParam(name = "search", required = false) String search) {
        Page<Event> envetPage;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        if (search != null && !search.isEmpty()) {
            envetPage = eventService.searchEvent(affiliation, search, pageable);
        } else {
            envetPage = eventService.getEvents(affiliation, pageable);
        }
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, envetPage));
    }
}
