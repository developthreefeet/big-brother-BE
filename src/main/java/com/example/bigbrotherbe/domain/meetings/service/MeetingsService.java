package com.example.bigbrotherbe.domain.meetings.service;

import com.example.bigbrotherbe.domain.meetings.dto.request.MeetingsRegisterRequest;
import com.example.bigbrotherbe.domain.meetings.dto.request.MeetingsUpdateRequest;
import com.example.bigbrotherbe.domain.meetings.dto.response.MeetingsResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MeetingsService {
    void registerMeetings(MeetingsRegisterRequest meetingsRegisterRequest, List<MultipartFile> multipartFiles);

    void updateMeetings(Long meetingsId, MeetingsUpdateRequest meetingsUpdateRequest, List<MultipartFile> multipartFiles);

    void deleteMeetings(Long meetingsId);

    MeetingsResponse getMeetingsById(Long meetingsId);
}
