package com.example.bigbrotherbe.domain.meetings.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MeetingsUpdateRequest {
    private String title;
    private String content;

}
