package com.example.bigbrotherbe.global.file.entity;

import com.example.bigbrotherbe.domain.BaseTimeEntity;
import com.example.bigbrotherbe.domain.meetings.entity.Meetings;
import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.checker.units.qual.A;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileType;

    private String url;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "meetings_id")
    private Meetings meetings;

    public void update(String url) {
        this.url = url;
    }

    public void linkMeeting(Meetings meetings) {
        this.meetings = meetings;
    }
}
