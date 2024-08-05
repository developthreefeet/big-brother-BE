package com.example.bigbrotherbe.domain.faq.dto.response;

import com.example.bigbrotherbe.domain.faq.entity.FAQ;
import com.example.bigbrotherbe.domain.meetings.dto.response.MeetingsResponse;
import com.example.bigbrotherbe.domain.meetings.entity.Meetings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class FAQResponse {
    private Long faqId;
    private String title;
    private String content;
    private Long affiliationId;
    private List<String> urlList;

    public static FAQResponse fromFAQResponse(FAQ faq, List<String> urlList) {
        return FAQResponse.builder()
                .faqId(faq.getId())
                .title(faq.getTitle())
                .content(faq.getContent())
                .affiliationId(faq.getAffiliationId())
                .urlList(urlList)
                .build();
    }
}
