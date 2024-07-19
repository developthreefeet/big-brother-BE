package com.example.bigbrotherbe.member.entity.dto.request;


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
    private String memberName;
    private String memberPass;
}
