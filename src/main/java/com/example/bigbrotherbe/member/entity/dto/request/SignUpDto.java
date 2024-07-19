package com.example.bigbrotherbe.member.entity.dto.request;

import com.example.bigbrotherbe.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.Date;
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
