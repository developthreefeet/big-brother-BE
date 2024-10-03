package com.example.bigbrotherbe.global.file.entity;

import com.example.bigbrotherbe.domain.BaseTimeEntity;
import com.example.bigbrotherbe.domain.campusNotice.entity.CampusNotice;
import com.example.bigbrotherbe.domain.event.entity.Event;
import com.example.bigbrotherbe.domain.faq.entity.FAQ;
import com.example.bigbrotherbe.domain.meetings.entity.Meetings;
import com.example.bigbrotherbe.domain.notice.entity.Notice;
import com.example.bigbrotherbe.domain.rule.entity.Rule;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(name = "file_id")
    private Long id;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "url", nullable = false, columnDefinition = "TEXT")
    private String url;

    @Column(name = "file_name")
    private String fileName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "meetings_id")
    @JsonIgnore
    private Meetings meetings;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "faq_id")
    @JsonIgnore
    private FAQ faq;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "notice_id")
    @JsonIgnore
    private Notice notice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    @JsonIgnore
    private Event event;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rule_id")
    @JsonIgnore
    private Rule rule;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "campus_notice_id")
    @JsonIgnore
    private CampusNotice campusNotice;


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

    public void linkEvent(Event event) {
        this.event = event;
    }

    public void linkRule(Rule rule) {
        this.rule = rule;
    }

    public void linkCampusNotice(CampusNotice campusNotice) {
        this.campusNotice = campusNotice;
    }
}
