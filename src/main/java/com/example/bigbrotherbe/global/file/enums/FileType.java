package com.example.bigbrotherbe.global.file.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType {
    NOTICE(1, "NOTICE"),
    FAQ(2, "FAQ"),
    MEETINGS(3, "MEETINGS"),
    EVENT(4, "EVENT"),
    RULE(5, "RULE"),
    AFFILIATION(6, "AFFILIATION");

    private final int val;
    private final String type;

}
