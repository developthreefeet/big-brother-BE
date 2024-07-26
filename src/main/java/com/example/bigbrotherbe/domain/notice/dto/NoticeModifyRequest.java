package com.example.bigbrotherbe.domain.notice.dto;

import com.example.bigbrotherbe.domain.notice.entity.Notice;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeModifyRequest {
    private String title;
    private String content;

}
