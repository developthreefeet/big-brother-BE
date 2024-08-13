package com.example.bigbrotherbe.global.file.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType {
    NOTICE(1, "notice"),
    FAQ(2, "faq"),
    MEETINGS(3, "meetings"),
    EVENT(4, "event"),
    RULE(5, "rule"),
    AFFILIATION(6, "affiliation"),
    CAMPUS_NOTICE(7, "campusNotice"),
    TRANSACTIONS(8, "transactions");

    private final int val;
    private final String type;

}
