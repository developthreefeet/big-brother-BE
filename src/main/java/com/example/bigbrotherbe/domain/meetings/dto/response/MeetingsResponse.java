package com.example.bigbrotherbe.domain.meetings.dto.response;

import com.example.bigbrotherbe.domain.meetings.entity.Meetings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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
    private List<String> urlList;

    public static MeetingsResponse fromMeetingsResponse(Meetings meetings, List<String> urlList) {
        return MeetingsResponse.builder()
                .meetingsId(meetings.getId())
                .title(meetings.getTitle())
                .content(meetings.getContent())
                .isPublic(meetings.isPublic())
                .affiliationId(meetings.getAffiliationId())
                .urlList(urlList)
                .build();
    }
}
