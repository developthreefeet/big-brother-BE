package com.example.bigbrotherbe.domain.event.service;

import com.example.bigbrotherbe.domain.event.dto.request.EventRegisterRequest;
import com.example.bigbrotherbe.domain.event.dto.request.EventUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {

    void registerEvent(EventRegisterRequest eventRegisterRequest, List<MultipartFile> multipartFiles);
    void updateEvent(Long eventId, EventUpdateRequest eventUpdateRequest, List<MultipartFile> multipartFiles);
}
