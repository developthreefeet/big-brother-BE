package com.example.bigbrotherbe.domain.member.entity.role;

import com.example.bigbrotherbe.domain.member.entity.enums.AffiliationCode;
import com.example.bigbrotherbe.domain.member.entity.enums.CouncilType;
import com.example.bigbrotherbe.domain.member.entity.enums.Role;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor

public class AffiliationListDto {
    private final List<AffiliationType> affiliationTypeList = new ArrayList<>();
    private final String memberName;


    public void addAffiliation(String councilType,String affiliation, String role) {
        try {
            affiliationTypeList.add(AffiliationType
                .builder()
                .councilType(CouncilType.fromcouncilType(councilType))
                .affiliationCode(AffiliationCode.fromcouncilName(affiliation))
                .role(Role.fromRole(role)).build());
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
