package com.example.bigbrotherbe.domain.meetings.entity;

import com.example.bigbrotherbe.domain.BaseTimeEntity;
import com.example.bigbrotherbe.global.file.entity.File;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Meetings extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meetings_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "is_public")
    private boolean isPublic;

    @Column(name = "affiliation_id")
    private Long affiliationId;

    @JsonIgnore
    @OneToMany(mappedBy = "meetings", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<File> files = new ArrayList<>();

    public void update(String title, String content, boolean isPublic, List<File> files) {
        this.title = title;
        this.content = content;
        this.isPublic = isPublic;
        if (files != null) {
            this.files.addAll(files);
            files.forEach(file -> file.linkMeeting(this));
        }
    }
}
