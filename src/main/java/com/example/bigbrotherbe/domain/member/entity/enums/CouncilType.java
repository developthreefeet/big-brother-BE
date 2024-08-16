package com.example.bigbrotherbe.domain.member.entity.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum CouncilType {
    GENERAL_STUDENTS_ASSOCIATION("총학"),
    COLLEGE("단과대"),
    DEPARTMENT("학과");
    private final String councilType;
    CouncilType(String councilType){
        this.councilType = councilType;
    }

    @JsonValue
    public String serializationValue() {
        return this.councilType;
    }

    public static CouncilType fromcouncilType(String councilType) {
        for (CouncilType type : CouncilType.values()) {
            if (type.councilType.equals(councilType)) {
                return type;
            }
        }
        throw new IllegalArgumentException("없는 CouncilType입니다." + councilType);
    }
}
