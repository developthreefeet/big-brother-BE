package com.example.bigbrotherbe.domain.rule.entity;

import com.example.bigbrotherbe.domain.BaseTimeEntity;
import com.example.bigbrotherbe.file.entity.File;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rule extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    private Long id;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(name = "affiliation_id")
    private Long affiliationId;

    @JsonIgnore
    @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<File> files = new ArrayList<>();

    public void update(String title, List<File> files) {
        this.title = title;
        if (files != null) {
            this.files.addAll(files);
            files.forEach(file -> file.linkRule(this));
        }
    }
}
