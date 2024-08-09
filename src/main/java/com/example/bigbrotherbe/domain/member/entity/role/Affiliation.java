package com.example.bigbrotherbe.domain.member.entity.role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Affiliation {
    @Id
    private long affiliation_id;

    @Column
    private String studentCouncil;

    @Column
    private String affiliationName;

    @Column
    private String presidentName;
}
