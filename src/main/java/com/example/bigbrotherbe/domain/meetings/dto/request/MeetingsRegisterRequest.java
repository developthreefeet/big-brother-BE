package com.example.bigbrotherbe.domain.meetings.dto.request;

import com.example.bigbrotherbe.domain.meetings.entity.Meetings;

import com.example.bigbrotherbe.file.entity.File;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MeetingsRegisterRequest {
    private String title;
    private String content;
    private Long affiliationId;

    public Meetings toMeetingsEntity(List<File> files) {
        return Meetings.builder()
                .title(title)
                .content(content)
                .isPublic(true)
                .affiliationId(affiliationId)
                .files(files)
                .build();
    }
}
