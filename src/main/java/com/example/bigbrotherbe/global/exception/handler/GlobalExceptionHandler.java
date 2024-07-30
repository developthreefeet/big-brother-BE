package com.example.bigbrotherbe.global.exception.handler;

import com.example.bigbrotherbe.global.exception.BusinessException;
import com.example.bigbrotherbe.global.exception.response.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ErrorCode 내의 에러
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ExceptionResponse> handleBusinessException(BusinessException e) {
        log.error("BusinessException", e);
        ExceptionResponse exceptionResponse = ExceptionResponse.of(e.getErrorCode());

        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(exceptionResponse);
    }
}
