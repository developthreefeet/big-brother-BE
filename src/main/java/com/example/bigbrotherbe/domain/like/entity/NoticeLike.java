package com.example.bigbrotherbe.domain.like.entity;

import com.example.bigbrotherbe.domain.like.entity.Key.NoticeLikeId;
import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.domain.notice.entity.Notice;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(NoticeLikeId.class)
@Table(name = "likes")
public class NoticeLike {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    @JsonIgnore
    private Notice notice;

//    @Id
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "meetings_id", nullable = true)
//    @JsonIgnore
//    private Meetings meetings;
//
//    @Id
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "faq_id", nullable = true)
//    @JsonIgnore
//    private FAQ faq;
//
//    @Id
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "event_id", nullable = true)
//    @JsonIgnore
//    private Event event;
//
//    @Id
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "rule_id", nullable = true)
//    @JsonIgnore
//    private Rule rule;
//
//    @Id
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "campus_notice_id", nullable = true)
//    @JsonIgnore
//    private CampusNotice campusNotice;

    public void linkNotice(Notice notice) {
        this.notice = notice;
    }

}
