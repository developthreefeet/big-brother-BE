package com.example.bigbrotherbe.domain.faq.dto.response;

import com.example.bigbrotherbe.domain.faq.entity.FAQ;
import com.example.bigbrotherbe.global.file.dto.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class FAQResponse {
    private Long faqId;
    private String title;
    private String content;
    private Long affiliationId;
    private List<FileResponse> fileInfo;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public static FAQResponse fromFAQResponse(FAQ faq, List<FileResponse> fileInfo) {
        return FAQResponse.builder()
                .faqId(faq.getId())
                .title(faq.getTitle())
                .content(faq.getContent())
                .affiliationId(faq.getAffiliationId())
                .fileInfo(fileInfo)
                .createAt(faq.getCreateAt())
                .updateAt(faq.getUpdateAt())
                .build();
    }
}
