package com.example.bigbrotherbe.domain.notice.dto.request;

import com.example.bigbrotherbe.domain.notice.entity.Notice;
import com.example.bigbrotherbe.global.file.entity.File;
import lombok.*;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeRegisterRequest {
    private String title;
//    private String type;
    private String content;
    private Long affiliationId;

    public Notice toNoticeEntity(List<File> files){
        return Notice.builder()
                .title(title)
//                .type(type)
                .content(content)
                .affiliationId(affiliationId)
                .files(files)
                .build();
    }
}
