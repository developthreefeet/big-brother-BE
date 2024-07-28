package com.example.bigbrotherbe.global.file.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType {
    NOTICE(1, "공지사항"),
    FAQ(2, "FAQ"),
    MEETINGS(3, "회의록"),
    EVENT(4, "행사"),
    RULE(5, "회칙"),
    Affiliation(6, "학생회");

    private final int val;
    private final String type;

}
