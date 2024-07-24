package com.example.bigbrotherbe.domain.member.entity.role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
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
