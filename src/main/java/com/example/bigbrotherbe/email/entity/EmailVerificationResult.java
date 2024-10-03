package com.example.bigbrotherbe.email.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
