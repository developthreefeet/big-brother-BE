package com.example.bigbrotherbe.domain.event.entity;

import com.example.bigbrotherbe.global.entity.BaseTimeEntity;
import com.example.bigbrotherbe.global.file.entity.File;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false, name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "target")
    private String target;

    @Column(name = "start_datetime")
    private LocalDateTime startDateTime;

    @Column(name = "end_datetime")
    private LocalDateTime endDateTime;

    @Column(nullable = false, name = "affiliation_id")
    private Long affiliationId;

    @JsonIgnore
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<File> files = new ArrayList<>();

    public void update(String title, String content, String target, LocalDateTime startDateTime, LocalDateTime endDateTime, List<File> files) {
        this.title = title;
        this.content = content;
        this.target = target;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        if (files != null) {
            this.files.addAll(files);
            files.forEach(file -> file.linkEvent(this));
        }
    }
}
