package com.example.bigbrotherbe.domain.like.entity.Key;

import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.domain.notice.entity.Notice;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class NoticeLikeId implements Serializable {
    private Member member;
    private Notice notice;
}
