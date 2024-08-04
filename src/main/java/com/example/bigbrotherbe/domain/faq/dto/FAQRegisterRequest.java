package com.example.bigbrotherbe.domain.faq.dto;

import com.example.bigbrotherbe.domain.faq.entity.FAQ;
import com.example.bigbrotherbe.global.file.entity.File;
import lombok.*;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FAQRegisterRequest {
    private String title;
    private String content;
    private Long affiliationId;

    public FAQ toFAQEntity(List<File> files){
        return FAQ.builder()
                .title(title)
                .content(content)
                .affiliationId(affiliationId)
                .files(files)
                .build();
    }
}
