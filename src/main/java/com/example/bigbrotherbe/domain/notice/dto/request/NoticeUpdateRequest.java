package com.example.bigbrotherbe.domain.notice.dto.request;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeUpdateRequest {
    private String title;
    private String content;

}
