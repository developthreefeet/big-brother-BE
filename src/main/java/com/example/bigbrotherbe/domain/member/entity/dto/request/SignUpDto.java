package com.example.bigbrotherbe.domain.member.entity.dto.request;

import com.example.bigbrotherbe.domain.member.entity.Member;


import com.example.bigbrotherbe.global.exception.BusinessException;
import com.example.bigbrotherbe.global.exception.enums.ErrorCode;
import java.time.LocalDateTime;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpDto {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private String username;
    private String password;
    private String email;
    private String is_active;
    private String create_at;
    private String update_at;
    private String role;
    private String affiliation;

    public Member toEntity(SignUpDto signUpDto, String password) {
        if(!isVaildEmail(signUpDto.getEmail())){
            throw new BusinessException(ErrorCode.INVALID_EMAIL_FORMAT);
        }
        return Member.builder()
            .username(signUpDto.getUsername())
            .password(password)
            .email(signUpDto.getEmail())
            .is_active("true")
            .create_at(LocalDateTime.now())
            .update_at(LocalDateTime.now())
            .build();
    }

    private boolean isVaildEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
