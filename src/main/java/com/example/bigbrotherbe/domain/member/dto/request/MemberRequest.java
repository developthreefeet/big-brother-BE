package com.example.bigbrotherbe.domain.member.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberRequest {
    private String memberEmail;
    private String memberPass;
}
