package com.example.bigbrotherbe.global.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // MEMBER
    UNABLE_TO_SEND_EMAIL(HttpStatus.BAD_REQUEST, "MEMBER-001", "이메일을 보낼 수 없습니다."),
    EXIST_EMAIL(HttpStatus.BAD_REQUEST, "MEMBER-002", "이미 존재하는 이메일입니다."),
    NO_SUCH_ALGORITHM(HttpStatus.NOT_FOUND, "MEMBER-003", "알고리즘이 없습니다."),
    FAIL_LOAD_MEMBER(HttpStatus.EXPECTATION_FAILED, "MEMBER-004", "멤버를 불러오는 데 실패했습니다."),
    NO_EXIST_EMAIL(HttpStatus.NOT_FOUND, "MEMBER-005", "존재하지 않는 이메일입니다."),
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "MEMBER-006", "올바르지 않은 이메일 형식입니다."),

    // MEETINGS
    NO_EXIST_MEETINGS(HttpStatus.NOT_FOUND, "MEETINGS-001", "존재하지 않는 회의록 입니다."),

    // NOTICE
    NO_EXIST_NOTICE(HttpStatus.NOT_FOUND, "NOTICE-001", "존재하지 않는 공지사항 입니다."),

    // FAQ
    NO_EXIST_FAQ(HttpStatus.NOT_FOUND, "FAQ-001", "존재하지 않는 FAQ 입니다."),

    // EVENT
    NO_EXIST_EVENT(HttpStatus.NOT_FOUND, "EVENT-001", "존재하지 않는 행사 입니다."),

    // AFFILIATION
    NO_EXIST_AFFILIATION(HttpStatus.NOT_FOUND, "AFFILIATION-001", "존재하지 않는 학생회 입니다."),

    // FILE
    FAIL_TO_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "FILE-001", "파일 업로드에 실패하였습니다."),
    FAIL_TO_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "FILE-002", "파일 삭제에 실패하였습니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
