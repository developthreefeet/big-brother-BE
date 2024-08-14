
package com.example.bigbrotherbe.domain.member.dto.response;


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
    private final String college;
    private final String affiliation;

    public static MemberResponse form(Long id, String username, String email, LocalDateTime createAt, String college, String affiliation) {
        return MemberResponse
            .builder()
            .id(id)
            .memberName(username)
            .email(email)
            .create_at(createAt)
            .college(college)
            .affiliation(affiliation)
            .build();
    }
}
