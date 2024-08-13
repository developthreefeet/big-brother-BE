package com.example.bigbrotherbe.domain.member.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_PRESIDENT("ROLE_PRESIDENT"),
    ROLE_MANAGER("ROLE_MANAGER");

    private final String role;
}
