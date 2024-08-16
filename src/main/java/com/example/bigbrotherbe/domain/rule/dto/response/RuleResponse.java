package com.example.bigbrotherbe.domain.rule.dto.response;

import com.example.bigbrotherbe.domain.meetings.dto.response.MeetingsResponse;
import com.example.bigbrotherbe.domain.meetings.entity.Meetings;
import com.example.bigbrotherbe.domain.rule.entity.Rule;
import com.example.bigbrotherbe.global.file.dto.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RuleResponse {
    private Long ruleId;
    private String title;
    private Long affiliationId;
    private List<FileResponse> fileInfo;

    public static RuleResponse fromRuleResponse(Rule rule, List<FileResponse> fileInfo) {
        return RuleResponse.builder()
                .ruleId(rule.getId())
                .title(rule.getTitle())
                .affiliationId(rule.getAffiliationId())
                .fileInfo(fileInfo)
                .build();
    }
}
