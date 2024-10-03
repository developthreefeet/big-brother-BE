package com.example.bigbrotherbe.domain.faq.entity;

import com.example.bigbrotherbe.global.entity.BaseTimeEntity;
import com.example.bigbrotherbe.global.file.entity.File;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class FAQ extends BaseTimeEntity {
    @Id                                                 //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) //DB에서 값 자동 생성
    @Column(name = "faq_id", updatable = false, unique = true)
    private Long id;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false, name = "content", columnDefinition = "TEXT") // 긴 문자열
    private String content;

    @Column(nullable = false, name = "affiliation_id")
    private Long affiliationId;

    @OneToMany
    @JoinColumn(name = "faq_id")
    private List<File> files;

    public void update(String title, String content, List<File> files) {
        this.title = title;
        this.content = content;
        if (files != null) {
            this.files.addAll(files);
            files.forEach(file -> file.linkFAQ(this));
        }
    }
}
