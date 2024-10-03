package com.example.bigbrotherbe.domain.meetings.dto.response;

import com.example.bigbrotherbe.domain.meetings.entity.Meetings;
import com.example.bigbrotherbe.file.dto.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MeetingsResponse {
    private Long meetingsId;
    private String title;
    private String content;
    private boolean isPublic;
    private Long affiliationId;
    private List<FileResponse> fileInfo;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public static MeetingsResponse fromMeetingsResponse(Meetings meetings, List<FileResponse> fileInfo) {
        return MeetingsResponse.builder()
                .meetingsId(meetings.getId())
                .title(meetings.getTitle())
                .content(meetings.getContent())
                .isPublic(meetings.isPublic())
                .affiliationId(meetings.getAffiliationId())
                .fileInfo(fileInfo)
                .createAt(meetings.getCreateAt())
                .updateAt(meetings.getUpdateAt())
                .build();
    }
}
