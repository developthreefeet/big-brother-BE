package com.example.bigbrotherbe.global.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // SUCCESS
    SUCCESS(HttpStatus.OK, "SUCCESS", "요청에 성공하였습니다."),

    // MEMBER
    UNABLE_TO_SEND_EMAIL(HttpStatus.BAD_REQUEST, "MEMBER-001", "이메일을 보낼 수 없습니다."),
    EXIST_EMAIL(HttpStatus.BAD_REQUEST, "MEBER-002", "이미 존재하는 이메일입니다."),
    NO_SUCH_ALGORITHM(HttpStatus.NOT_FOUND, "MEMBER-003", "알고리즘이 없습니다."),

    // MEETINGS
    NO_EXIST_MEETINGS(HttpStatus.NOT_FOUND, "MEETINGS-001", "존재하지 않는 회의록 입니다."),

    // AFFILIATION
    NO_EXIST_AFFILIATION(HttpStatus.NOT_FOUND, "AFFILIATION-001", "존재하지 않는 학생회 입니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
