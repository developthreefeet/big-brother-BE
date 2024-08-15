package com.example.bigbrotherbe.domain.member.entity.role;

import com.example.bigbrotherbe.domain.member.entity.enums.AffiliationCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Affiliation {
    @Id
    private Long affiliation_id;

    @Column
    private String councilType;

    @Column
    private String studentCouncil;

    @Column
    private String name;

    @Column
    private String presidentName;

    public static Affiliation toEntity(AffiliationCode affiliationCode) {
        return Affiliation.builder().affiliation_id((long)affiliationCode.getVal()).councilType(affiliationCode.getCouncilType()).name(affiliationCode.getName()).build();
    }
}
