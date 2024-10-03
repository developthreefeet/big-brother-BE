package com.example.bigbrotherbe.domain.campusNotice.dto;

import com.example.bigbrotherbe.domain.campusNotice.entity.CampusNotice;
import com.example.bigbrotherbe.domain.campusNotice.entity.CampusNoticeType;
import com.example.bigbrotherbe.file.dto.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CampusNoticeResponse {
    private Long noticeId;
    private String title;
    private String content;
    private CampusNoticeType type;
    private List<FileResponse> fileInfo;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public static CampusNoticeResponse fromCampusNoticeResponse(CampusNotice campusNotice, List<FileResponse> fileInfo) {
        return CampusNoticeResponse.builder()
                .noticeId(campusNotice.getId())
                .title(campusNotice.getTitle())
                .content(campusNotice.getContent())
                .type(campusNotice.getType())
                .fileInfo(fileInfo)
                .createAt(campusNotice.getCreateAt())
                .updateAt(campusNotice.getUpdateAt())
                .build();
    }
}
