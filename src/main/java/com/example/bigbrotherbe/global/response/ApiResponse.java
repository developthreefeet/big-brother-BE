package com.example.bigbrotherbe.global.response;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

@Builder
@JsonPropertyOrder
public record ApiResponse<T>(
    String status,
    T data,
    String message
) {
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";

    public static <T> ApiResponse<T> ok(T data){
        return ApiResponse.<T>builder()
            .status(SUCCESS)
            .data(data)
            .build();
    }

    public static <T> ApiResponse<T> error(String errorMessage){
        return ApiResponse.<T>builder()
            .status(ERROR)
            .message(errorMessage)
            .build();
    }


}
