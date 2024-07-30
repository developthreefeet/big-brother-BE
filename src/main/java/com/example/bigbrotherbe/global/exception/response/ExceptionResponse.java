package com.example.bigbrotherbe.global.exception.response;

import com.example.bigbrotherbe.global.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExceptionResponse {
    private HttpStatus status;                 // 에러 상태 코드
    private String divisionCode;        // 에러 구분 코드
    private String resultMsg;           // 에러 메시지

    @Builder
    protected ExceptionResponse(final ErrorCode code) {
        this.resultMsg = code.getMessage();
        this.status = code.getHttpStatus();
        this.divisionCode = code.getErrorCode();
    }

    public static ExceptionResponse of(final ErrorCode code) {
        return new ExceptionResponse(code);
    }
}
