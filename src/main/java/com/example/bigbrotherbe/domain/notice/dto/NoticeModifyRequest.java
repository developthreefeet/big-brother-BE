package com.example.bigbrotherbe.domain.notice.dto;

import com.example.bigbrotherbe.domain.notice.entry.Notice;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeModifyRequest {


    public Notice toEntity(){
        return Notice.builder().

                build();
    }
}
