package com.example.bigbrotherbe.domain.event.service;

import com.example.bigbrotherbe.domain.event.dto.request.EventRegisterRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {

    void registerEvent(EventRegisterRequest eventRegisterRequest, List<MultipartFile> multipartFiles);
}
