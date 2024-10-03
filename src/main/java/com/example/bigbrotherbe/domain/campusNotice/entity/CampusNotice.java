package com.example.bigbrotherbe.domain.campusNotice.entity;

import com.example.bigbrotherbe.file.entity.File;
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

    @Column(nullable = false, name = "content", columnDefinition = "MEDIUMTEXT")
    private String content;

    @OneToMany
    @JoinColumn(name = "campus_notice_id")
    private List<File> files;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;
}
