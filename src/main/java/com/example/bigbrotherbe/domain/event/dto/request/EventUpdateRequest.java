package com.example.bigbrotherbe.domain.event.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class EventUpdateRequest {
    private String title;
    private String content;
    private String target;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
