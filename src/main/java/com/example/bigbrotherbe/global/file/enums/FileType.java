package com.example.bigbrotherbe.global.file.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileType {
    NOTICE(1,"공지사항"),
    FAQ(2,"FAQ"),
    MEETINGS(3, "회의록"),
    EVENT(4,"행사"),
    RULE(5,"회칙"),
    MEMBER(6,"프로필"),

}
