package com.example.bigbrotherbe.domain.member.entity.enums;

import com.fasterxml.jackson.annotation.JsonValue;
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

    @JsonValue
    public String getRole() {
        return role;
    }

    @JsonValue
    public static Role fromRole(String role) {
        for (Role roleIns : Role.values()) {
            if (roleIns.role.equals(role)) {
                return roleIns;
            }
        }
        throw new IllegalArgumentException("없는 CouncilType입니다." + role);
    }
}
