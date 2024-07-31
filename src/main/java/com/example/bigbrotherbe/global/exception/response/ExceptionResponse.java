package com.example.bigbrotherbe.global.exception.response;

import com.example.bigbrotherbe.global.exception.enums.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExceptionResponse {
    private HttpStatus status;
    private String responseCode;
    private String resultMsg;

    @Builder
    protected ExceptionResponse(final ErrorCode code) {
        this.resultMsg = code.getMessage();
        this.status = code.getHttpStatus();
        this.responseCode = code.getErrorCode();
    }

    public static ExceptionResponse of(final ErrorCode code) {
        return new ExceptionResponse(code);
    }
}
