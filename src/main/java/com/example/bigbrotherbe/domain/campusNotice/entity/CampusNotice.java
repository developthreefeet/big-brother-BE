package com.example.bigbrotherbe.domain.campusNotice.entity;

import com.example.bigbrotherbe.global.file.entity.File;
import com.example.bigbrotherbe.domain.campusNotice.util.LambdaTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class CampusNotice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campus_notice_id", updatable = false, unique = true, nullable = false)
    private Long id;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false, name = "campus_notice_type")
    private CampusNoticeType type;

    @Column(nullable = false, name = "content", columnDefinition = "TEXT")
    private String content;

    @OneToMany
    @JoinColumn(name = "campus_notice_id")
    private List<File> files;

    @JsonDeserialize(using = LambdaTimeDeserializer.class)
    private LocalDateTime createAt;

    @JsonDeserialize(using = LambdaTimeDeserializer.class)
    private LocalDateTime updateAt;
}
