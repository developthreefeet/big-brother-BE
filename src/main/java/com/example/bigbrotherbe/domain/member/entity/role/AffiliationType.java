package com.example.bigbrotherbe.domain.member.entity.role;

import com.example.bigbrotherbe.domain.member.entity.enums.AffiliationCode;
import com.example.bigbrotherbe.domain.member.entity.enums.CouncilType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Builder
@Getter
public class AffiliationType {
    private final CouncilType councilType;
    private final Role role;
    private final AffiliationCode affiliationCode;

}
