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
    NO_EXIST_MEMBER(HttpStatus.NOT_FOUND, "MEMBER-007", "존재하지 않는 유저입니다."),
    MISMATCH_PASSWORD(HttpStatus.NOT_FOUND,"MEMBER-008", "비밀번호가 올바르지 않습니다."),
    NOT_COUNCIL_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER-008", "해당 학생회 멤버가 아닙니다."),
    NOT_PRESIDENT_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER-009", "해당 학생회 회장이 아닙니다."),


    // TOKEN
    ACCESS_Token_Expired(HttpStatus.BAD_REQUEST,  "TOKEN_001", "만료된 access 토큰입니다."),
    REFRESH_Token_Expired(HttpStatus.BAD_REQUEST,  "TOKEN_002", "만료된 refresh 토큰입니다."),

    // MEETINGS
    NO_EXIST_MEETINGS(HttpStatus.NOT_FOUND, "MEETINGS-001", "존재하지 않는 회의록 입니다."),

    // NOTICE
    NO_EXIST_NOTICE(HttpStatus.NOT_FOUND, "NOTICE-001", "존재하지 않는 공지사항 입니다."),

    // FAQ
    NO_EXIST_FAQ(HttpStatus.NOT_FOUND, "FAQ-001", "존재하지 않는 FAQ 입니다."),

    // EVENT
    NO_EXIST_EVENT(HttpStatus.NOT_FOUND, "EVENT-001", "존재하지 않는 행사 입니다."),

    // Rule
    NO_EXIST_RULE(HttpStatus.NOT_FOUND, "RULE-001", "존재하지 않는 회칙 입니다."),

    // AFFILIATION
    NO_EXIST_AFFILIATION(HttpStatus.NOT_FOUND, "AFFILIATION-001", "존재하지 않는 학생회 입니다."),
    INVALID_AFFILIATION(HttpStatus.BAD_REQUEST, "AFFILIATION-002", "해당 학생회는 단과대가 아닙니다."),

    // FILE
    FAIL_TO_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "FILE-001", "파일 업로드에 실패하였습니다."),
    FAIL_TO_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "FILE-002", "파일 삭제에 실패하였습니다."),

    // TRANSACTIONS
    FAIL_TO_LONG_PARSING(HttpStatus.BAD_REQUEST, "TRANSACTIONS-001", "long 타입 파싱에 실패했습니다."),
    EMPTY_FILE(HttpStatus.BAD_REQUEST, "TRANSACTIONS-002", "파일이 없습니다."),
    NO_EXIST_TRANSACTIONS(HttpStatus.NOT_FOUND, "TRANSACTIONS-003", "존재하지 않는 계좌내역 정보입니다."),

    // CAMPUS_NOTICE
    NO_EXIST_URL_ID(HttpStatus.NOT_FOUND, "CAMPUS_NOTICE-001", "유효하지 않은 URL ID 입니다."),
    NO_EXIST_CAMPUS_NOTICE(HttpStatus.NOT_FOUND, "CAMPUS_NOTICE-002", "존재하지 않는 학교 공지사항 입니다."),
    FAIL_TO_JSON_PARSING(HttpStatus.BAD_REQUEST, "CAMPUS_NOTICE-003", "공지사항 페이지의 JSON 파싱에 실패하였습니다."),
    LAMBDA_FUNCTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "CAMPUS_NOTICE-004", "AWS Lambda 함수에서 오류가 발생했습니다."),

    // VERIFICATION
    MISMATCH_VERIFIED_CODE(HttpStatus.NOT_FOUND, "VERIFICATION-001", "이메일 인증 코드가 일치하지 않습니다."),

    // PATTERN
    ILLEGAL_HEADER_PATTERN(HttpStatus.BAD_REQUEST, "PATTERN-001", "헤더의 형식이 맞지 않습니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
