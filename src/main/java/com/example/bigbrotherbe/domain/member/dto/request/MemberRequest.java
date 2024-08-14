package com.example.bigbrotherbe.domain.member.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Schema(title = "MEMBER_REQ_02 : 로그인 요청")
public class MemberRequest {
    @Schema(description = "사용자 이메일", example = "gkstkddbs99@mju.ac.kr")
    private String memberEmail;
    @Schema(description = "사용자 비밀번호", example = "test1234!")
    private String memberPass;
}
