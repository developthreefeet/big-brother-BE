package com.example.bigbrotherbe.domain.notice.dto.response;

import com.example.bigbrotherbe.domain.notice.entity.Notice;
import com.example.bigbrotherbe.global.file.dto.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class NoticeResponse {
    private Long noticeId;
    private String title;
    private String content;
    private Long affiliationId;
    private List<FileResponse> fileInfo;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public static NoticeResponse fromNoticeResponse(Notice notice, List<FileResponse> fileInfo) {
        return NoticeResponse.builder()
                .noticeId(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .affiliationId(notice.getAffiliationId())
                .fileInfo(fileInfo)
                .createAt(notice.getCreateAt())
                .updateAt(notice.getUpdateAt())
                .build();
    }
}
