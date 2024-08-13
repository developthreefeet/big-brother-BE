package com.example.bigbrotherbe.domain.member.entity.dto.response;

import com.example.bigbrotherbe.domain.member.entity.role.AffiliationMap;
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
        private final AffiliationMap affiliationMap;

        public static MemberInfoResponse form(Long id, String username, String email, LocalDateTime createAt, LocalDateTime updateAt,AffiliationMap affiliationMap) {
            return MemberInfoResponse
                .builder()
                .memberName(username)
                .email(email)
                .createAt(createAt)
                .updateAt(updateAt)
                .affiliationMap(affiliationMap)
                .build();
        }

    }


