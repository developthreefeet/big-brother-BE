package com.example.bigbrotherbe.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AffiliationResponse {
    private int val;
    private String councilName;

    public static AffiliationResponse fromAffiliationResponse(int val, String councilName) {
        return AffiliationResponse.builder()
                .val(val)
                .councilName(councilName)
                .build();
    }
}
