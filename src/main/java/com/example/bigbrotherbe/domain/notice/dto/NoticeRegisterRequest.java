package com.example.bigbrotherbe.domain.notice.dto;

import com.example.bigbrotherbe.domain.notice.entity.Notice;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeRegisterRequest {
    private String title;
    private String type;
    private String content;
    private Long affiliationId;

    public Notice toNoticeEntity(){
        return Notice.builder()
                .title(title)
                .type(type)
                .content(content)
                .affiliationId(affiliationId)
                .build();
    }
}
