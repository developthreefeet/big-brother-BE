package com.example.bigbrotherbe.domain.event.service;

import com.example.bigbrotherbe.domain.event.dto.request.EventRegisterRequest;
import com.example.bigbrotherbe.domain.event.dto.request.EventUpdateRequest;
import com.example.bigbrotherbe.domain.event.dto.response.EventResponse;
import com.example.bigbrotherbe.domain.event.entity.Event;
import com.example.bigbrotherbe.domain.meetings.entity.Meetings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {

    void registerEvent(EventRegisterRequest eventRegisterRequest, List<MultipartFile> multipartFiles);

    void updateEvent(Long eventId, EventUpdateRequest eventUpdateRequest, List<MultipartFile> multipartFiles);

    void deleteEvent(Long eventId);

    EventResponse getEventById(Long eventId);

    Page<Event> getEvents(Long affiliationId, Pageable pageable);

    Page<Event> searchEvent(Long affiliationId, String title, Pageable pageable);
}
