package com.example.bigbrotherbe.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AffiliationCollegeResponse {
    private int val;
    private String councilName;

    public static AffiliationCollegeResponse fromAffiliationCollegeResponse(int val, String councilName) {
        return AffiliationCollegeResponse.builder()
                .val(val)
                .councilName(councilName)
                .build();
    }
}
