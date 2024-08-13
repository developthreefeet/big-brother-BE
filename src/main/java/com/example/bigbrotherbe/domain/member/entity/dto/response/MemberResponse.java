
package com.example.bigbrotherbe.domain.member.entity.dto.response;


import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MemberResponse {
    private final Long id;
    private final String memberName;
    private final String email;
    private final LocalDateTime create_at;
    public static MemberResponse form(Long id, String username, String email, LocalDateTime createAt) {
        return MemberResponse
            .builder()
            .id(id)
            .memberName(username)
            .email(email)
            .create_at(createAt)
            .build();
    }
}
