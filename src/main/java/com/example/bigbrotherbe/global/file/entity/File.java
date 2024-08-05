package com.example.bigbrotherbe.global.file.entity;

import com.example.bigbrotherbe.domain.BaseTimeEntity;
import com.example.bigbrotherbe.domain.event.entity.Event;
import com.example.bigbrotherbe.domain.faq.entity.FAQ;
import com.example.bigbrotherbe.domain.meetings.entity.Meetings;
import com.example.bigbrotherbe.domain.notice.entity.Notice;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "faq_id")
    private FAQ faq;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    private Event event;


    public void update(String url) {
        this.url = url;
    }

    public void linkMeeting(Meetings meetings) {
        this.meetings = meetings;
    }

    public void linkFAQ(FAQ faq) {
        this.faq = faq;
    }

    public void linkNotice(Notice notice) {
        this.notice = notice;
    }

    public void linkEvent(Event evnet) {
        this.event = evnet;
    }
}
