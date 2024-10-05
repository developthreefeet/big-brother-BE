package com.example.bigbrotherbe.domain.event.controller;


import com.example.bigbrotherbe.domain.event.dto.response.EventResponse;
import com.example.bigbrotherbe.domain.event.entity.Event;
import com.example.bigbrotherbe.domain.event.service.EventService;
import com.example.bigbrotherbe.global.common.exception.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static com.example.bigbrotherbe.global.common.constant.Constant.GetContent.PAGE_DEFAULT_VALUE;
import static com.example.bigbrotherbe.global.common.constant.Constant.GetContent.SIZE_DEFAULT_VALUE;
import static com.example.bigbrotherbe.global.common.exception.enums.SuccessCode.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event")
public class EventController {

    private final EventService eventService;

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
