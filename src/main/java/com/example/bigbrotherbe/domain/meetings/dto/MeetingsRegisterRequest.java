package com.example.bigbrotherbe.domain.meetings.dto;

import com.example.bigbrotherbe.domain.meetings.entity.Meetings;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MeetingsRegisterRequest {
    private String title;
    private String content;

    public Meetings toMeetingsEntity() {
        return Meetings.builder()
                .title(title)
                .content(content)
                .build();
    }
}
