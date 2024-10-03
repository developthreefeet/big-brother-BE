package com.example.bigbrotherbe.domain.event.dto.response;

import com.example.bigbrotherbe.domain.event.entity.Event;
import com.example.bigbrotherbe.file.dto.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class EventResponse {
    private Long eventId;
    private String title;
    private String content;
    private String target;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private Long affiliationId;
    private List<FileResponse> fileInfo;

    public static EventResponse fromEventResponse(Event event, List<FileResponse> fileInfo) {
        return EventResponse.builder()
                .eventId(event.getId())
                .title(event.getTitle())
                .content(event.getContent())
                .target(event.getTarget())
                .startDateTime(event.getStartDateTime())
                .endDateTime(event.getEndDateTime())
                .affiliationId(event.getAffiliationId())
                .createAt(event.getCreateAt())
                .updateAt(event.getUpdateAt())
                .fileInfo(fileInfo)
                .build();
    }
}
