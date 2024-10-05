package com.example.bigbrotherbe.global.auth.jwt.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class TokenDto {
    public static final String ACCESS_TOKEN = "Access";
    public static final String REFRESH_TOKEN = "Refresh";

    private final String accessToken;
    private final String refreshToken;

}