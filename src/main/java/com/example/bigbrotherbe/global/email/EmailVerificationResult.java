package com.example.bigbrotherbe.global.email;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerificationResult {
    private boolean authResult;
    public static EmailVerificationResult of(boolean authResult) {
        return EmailVerificationResult.builder().authResult(authResult).build();
    }
}
