package com.example.bigbrotherbe.domain.rule.dto.request;

import com.example.bigbrotherbe.domain.rule.entity.Rule;
import com.example.bigbrotherbe.file.entity.File;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RuleRegisterRequest {
    private String title;
    private Long affiliationId;

    public Rule toRuleEntity(List<File> files) {
        return Rule.builder()
                .title(title)
                .affiliationId(affiliationId)
                .files(files)
                .build();
    }
}
