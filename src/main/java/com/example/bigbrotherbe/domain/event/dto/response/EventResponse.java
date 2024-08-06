package com.example.bigbrotherbe.domain.event.dto.response;

import com.example.bigbrotherbe.domain.event.entity.Event;
import com.example.bigbrotherbe.domain.meetings.dto.response.MeetingsResponse;
import com.example.bigbrotherbe.domain.meetings.entity.Meetings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class EventResponse {
    private Long meetingsId;
    private String title;
    private String content;
    private String target;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Long affiliationId;
    private List<String> urlList;

    public static EventResponse fromEventResponse(Event event, List<String> urlList) {
        return EventResponse.builder()
                .meetingsId(event.getId())
                .title(event.getTitle())
                .content(event.getContent())
                .target(event.getTarget())
                .startDateTime(event.getStartDateTime())
                .endDateTime(event.getEndDateTime())
                .affiliationId(event.getAffiliationId())
                .urlList(urlList)
                .build();
    }
}
