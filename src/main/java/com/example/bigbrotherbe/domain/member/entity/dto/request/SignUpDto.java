package com.example.bigbrotherbe.domain.member.entity.dto.request;

import com.example.bigbrotherbe.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpDto {

    private String username;
    private String password;
    private String email;
    private String is_active;
    private String create_at;
    private String update_at;
    private String roles;

    public Member toEntity(SignUpDto signUpDto, String password, List<String> roles) {
        return Member.builder().username(signUpDto.getUsername()).password(password).email(signUpDto.getEmail())
            .is_active("true").create_at(LocalDateTime.now()).update_at(LocalDateTime.now()).roles(roles).build();
    }
}
