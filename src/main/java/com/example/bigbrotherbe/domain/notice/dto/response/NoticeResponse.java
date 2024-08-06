package com.example.bigbrotherbe.domain.notice.dto.response;

import com.example.bigbrotherbe.domain.notice.entity.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class NoticeResponse {
    private Long noticeId;
    private String title;
    private String content;
    private Long affiliationId;
    private List<String> urlList;

    public static NoticeResponse fromNoticeResponse(Notice notice, List<String> urlList) {
        return NoticeResponse.builder()
                .noticeId(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .affiliationId(notice.getAffiliationId())
                .urlList(urlList)
                .build();
    }
}
