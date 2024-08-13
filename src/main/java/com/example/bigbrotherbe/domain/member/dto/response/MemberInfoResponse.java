package com.example.bigbrotherbe.domain.member.dto.response;

import com.example.bigbrotherbe.domain.member.entity.role.AffiliationListDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MemberInfoResponse {
        private final String memberName;
        private final String email;
        private final LocalDateTime createAt;
        private final LocalDateTime updateAt;
        private final AffiliationListDto affiliationListDto;

        public static MemberInfoResponse form(Long id, String username, String email, LocalDateTime createAt, LocalDateTime updateAt,
            AffiliationListDto affiliationListDto) {
            return MemberInfoResponse
                .builder()
                .memberName(username)
                .email(email)
                .createAt(createAt)
                .updateAt(updateAt)
                .affiliationListDto(affiliationListDto)
                .build();
        }

    }


