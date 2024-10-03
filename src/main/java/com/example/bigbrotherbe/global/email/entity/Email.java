package com.example.bigbrotherbe.global.email.entity;


import lombok.Builder;

@Builder
public record Email(String title, String authCode, String toEmailAddress) {

}
