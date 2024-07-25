package com.example.bigbrotherbe.domain.member.entity.dto.response;


import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MemberResponse {
    private final Long id;
    private final String memberName;
    private final String email;
    private final LocalDateTime create_at;
    private final LocalDateTime update_at;
    private final List<String> roles;

    public static MemberResponse form(Long id, String username, String email, LocalDateTime createAt) {
        return MemberResponse.builder().id(id).memberName(username).email(email).create_at(createAt)
            .build();
    }
}
