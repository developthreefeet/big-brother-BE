package com.example.bigbrotherbe.domain.member.entity.role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Affiliation {
    @Id
    private Long affiliation_id;

    @Column
    private String studentCouncil;

    @Column
    private String affiliationName;

    @Column
    private String presidentName;
}
