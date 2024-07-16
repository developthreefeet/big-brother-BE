package com.example.bigbrotherbe.domain.meetings.service;

import com.example.bigbrotherbe.domain.meetings.repository.MeetingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingsService {

    private final MeetingsRepository meetingsRepository;
}
