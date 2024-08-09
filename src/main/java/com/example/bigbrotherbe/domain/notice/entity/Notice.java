package com.example.bigbrotherbe.domain.notice.entity;

import com.example.bigbrotherbe.domain.BaseTimeEntity;
import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.global.file.entity.File;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity                                         // 데이터베이스 테이블과 매핑
@Getter                                         // lombok 라이브러리 -> 모든 필드 대상
@Setter                                         // "
@NoArgsConstructor(access = AccessLevel.PUBLIC) // 매개변수 없는 기본 생성자 공개 레벨로 생성
@AllArgsConstructor                             // 모든 매개변수 생성자 -> access 지정 안하면 접근 권한 공개로 설정
@Builder                                        // 빌더 패턴으로 초기화할 수 있게 만듦
@EqualsAndHashCode(of = "id")                   // equals, hashcode 메서드 자동 생성
public class Notice extends BaseTimeEntity {
    @Id                                                 //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) //DB에서 값 자동 생성
    @Column(name = "notice_id", updatable = false, unique = true, nullable = false)
    private Long id;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false, name = "content", columnDefinition = "TEXT") // 긴 문자열
    private String content;

    @Column
    private Long affiliationId;

    @OneToMany
    @JoinColumn(name = "notice_id")
    private List<File> files;

    //멤버랑은 여기서 연결 안하는게 맞나..?
//    @ManyToOne(fetch = FetchType.LAZY)                  // lazy loading
//    @JoinColumn(name = "member_id", nullable = false)
//    private Member member;

    public void update(String title, String content, List<File> files) {
        this.title = title;
        this.content = content;
        if (files != null) {
            this.files.addAll(files);
            files.forEach(file -> file.linkNotice(this));
        }
    }
}
