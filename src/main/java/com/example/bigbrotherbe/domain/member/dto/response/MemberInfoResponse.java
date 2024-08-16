package com.example.bigbrotherbe.domain.member.dto.response;

import com.example.bigbrotherbe.domain.member.dto.AffiliationListDto;
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
    }


