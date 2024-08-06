package com.example.bigbrotherbe.domain.faq.dto.request;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FAQModifyRequest {
    private String title;
    private String content;
}
