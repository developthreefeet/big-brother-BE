package com.example.bigbrotherbe.domain.member.entity.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberRequest {
    private String memberEmail;
    private String memberPass;
}
