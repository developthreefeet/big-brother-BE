package com.example.bigbrotherbe.domain.member.dto.request;

import com.example.bigbrotherbe.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDto {
    private Long id;
    private String username;
    private String email;
    private String is_active;
    private String create_at;
    private String update_at;

    static public MemberDto toDto(Member member){
        return MemberDto.builder().id(member.getId())
            .username(member.getUsername())
            .email(member.getEmail())
            .is_active(member.getIs_active())
            .create_at(member.getCreateAt().toString())
            .update_at(member.getUpdateAt().toString()).build();
    }
}
