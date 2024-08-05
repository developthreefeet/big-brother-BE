package com.example.bigbrotherbe.domain.event.dto.request;

import com.example.bigbrotherbe.domain.event.entity.Event;
import com.example.bigbrotherbe.domain.meetings.entity.Meetings;
import com.example.bigbrotherbe.global.file.entity.File;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class EventRegisterRequest {

    private String title;
    private String content;
    private String target;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Long affiliationId;

    public Event toEventEntity(List<File> files) {
        return Event.builder()
                .title(title)
                .content(content)
                .target(target)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .affiliationId(affiliationId)
                .files(files)
                .build();
    }
}
