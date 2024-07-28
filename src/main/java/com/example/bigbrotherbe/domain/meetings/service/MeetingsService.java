package com.example.bigbrotherbe.domain.meetings.service;

import com.example.bigbrotherbe.domain.meetings.dto.MeetingsRegisterRequest;
import com.example.bigbrotherbe.domain.meetings.dto.MeetingsUpdateRequest;

public interface MeetingsService {
    void registerMeetings(MeetingsRegisterRequest meetingsRegisterRequest);

    void updateMeetings(Long meetingsId, MeetingsUpdateRequest meetingsUpdateRequest);
}
