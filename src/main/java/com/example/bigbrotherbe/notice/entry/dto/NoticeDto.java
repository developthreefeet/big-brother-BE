package com.example.bigbrotherbe.notice.entry.dto;

import com.example.bigbrotherbe.member.entity.Member;
import com.example.bigbrotherbe.notice.entry.Notice;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeDto {
    private Long id;
    private String title;
    private String type;
    private String content;
    private String create_at;
    private String update_at;
//    private String affiliation;
    private Member member;

    static public NoticeDto toDto(Notice notice){
        return NoticeDto.builder().id(notice.getId())
                .title(notice.getTitle())
                .type(notice.getType())
                .content(notice.getContent())
                .create_at(notice.getCreate_at().toString())
                .update_at(notice.getUpdate_at().toString())
//                .affiliation(notice.getAffiliation())
//                .member(notice.getMember())   //member는 Dto 객체로 바꿔서?
                .build();
    }
}
