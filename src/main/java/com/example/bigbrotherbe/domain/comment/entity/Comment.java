package com.example.bigbrotherbe.domain.comment.entity;

import com.example.bigbrotherbe.domain.event.entity.Event;
import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.domain.notice.entity.Notice;
import com.example.bigbrotherbe.global.common.enums.EntityType;
import com.example.bigbrotherbe.global.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE comment SET deleted = true WHERE comment_id = ?")
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "content")
    private String content;

    @Enumerated // enum type 명시
    private EntityType entityType;

    @Column(nullable = false)
    private boolean deleted = Boolean.FALSE;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member members;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "notice_id")
    @JsonIgnore
    private Notice notice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    @JsonIgnore
    private Event event;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "rule_id")
//    @JsonIgnore
//    private Rule rule;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "campus_notice_id")
//    @JsonIgnore
//    private CampusNotice campusNotice;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "meetings_id")
//    @JsonIgnore
//    private Meetings meetings;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "faq_id")
//    @JsonIgnore
//    private FAQ faq;

    public void update(String content) {
        this.content = content;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public void linkNotice(Notice notice) {
        this.notice = notice;
    }

    public void linkEvent(Event event) {
        this.event = event;
    }


//        public void linkMeeting(Meetings meetings) {
//        this.meetings = meetings;
//    }

//    public void linkFAQ(FAQ faq) {
//        this.faq = faq;
//    }

//    public void linkRule(Rule rule) {
//        this.rule = rule;
//    }

//    public void linkCampusNotice(CampusNotice campusNotice) {
//        this.campusNotice = campusNotice;
//    }
}
