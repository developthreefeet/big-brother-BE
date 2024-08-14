package com.example.bigbrotherbe.domain.member.entity.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum AffiliationCode {

    // 총학
    STUDENT_COUNCIL(1, "총학", "총학생회"),

    // 인문대
    HUMANITIES(2, "단과대", "인문대학"),
    KOREAN_LANGUAGE_AND_LITERATURE(3, "학과", "국어국문학과"),
    CREATIVE_WRITING(4, "학과", "문예창작학과"),
    ENGLISH_LANGUAGE_AND_LITERATURE(5, "학과", "영어영문학과"),
    CHINESE_LANGUAGE_AND_LITERATURE(6, "학과", "중어중문학과"),
    JAPANESE_LANGUAGE_AND_LITERATURE(7, "학과", "일어일문학과"),
    LIBRARY_AND_INFORMATION_SCIENCE(8, "학과", "문헌정보학과"),
    ART_HISTORY(9, "학과", "미술사학과"),
    ARABIC_STUDIES(10, "학과", "아랍지역학과"),
    HISTORY(11, "학과", "사학과"),
    PHILOSOPHY(12, "학과", "철학과"),

    // 사회과학대
    SOCIAL_SCIENCE(13, "단과대", "사회과학대학"),
    PUBLIC_ADMINISTRATION(14, "학과", "행정학과"),
    ECONOMICS(15, "학과", "경제학과"),
    POLITICAL_SCIENCE_AND_DIPLOMACY(16, "학과", "정치외교학과"),
    DIGITAL_MEDIA(17, "학과", "디지털미디어학과"),
    CHILD_STUDIES(18, "학과", "아동학과"),
    YOUTH_GUIDANCE(19, "학과", "청소년지도학과"),

    // 경영대
    BUSINESS(20, "단과대", "경영대학"),
    BUSINESS_ADMINISTRATION(21, "학과", "경영학과"),
    MANAGEMENT_INFORMATION_SYSTEMS(22, "학과", "경영정보학과"),
    INTERNATIONAL_TRADE(23, "학과", "국제통상학과"),

    // 법대
    LAW(24, "단과대", "법학대학"),
    DEPARTMENT_OF_LAW(25, "학과", "법학과"),

    // ICT융합대
    ICT(26, "단과대", "ICT융합대학"),
    DIGITAL_CONTENTS_DESIGN(27, "학과", "디지털콘텐츠디자인학과"),
    SOFTWARE_APPLICATIONS(28, "학과", "응용소프트웨어전공"),
    DATA_TECHNOLOGY(29, "학과", "데이터테크놀로지전공");

    private final int val;
    private final String councilType;
    private final String councilName;


    public static AffiliationCode fromCouncilName(String councilName) {
        for (AffiliationCode affiliationCodeIns : AffiliationCode.values()) {
            if (affiliationCodeIns.councilName.equals(councilName)) {
                return affiliationCodeIns;
            }
        }
        throw new IllegalArgumentException("없는 단체 명입니다." + councilName);
    }

    public static List<AffiliationCode> getDepartmentsByCollege(AffiliationCode college) {
        return Arrays.stream(AffiliationCode.values())
                .filter(code -> code.getCouncilType().equals("학과") && code.getVal() > college.getVal() && code.getVal() <= getMaxValForCollege(college))
                .collect(Collectors.toList());
    }

    private static int getMaxValForCollege(AffiliationCode college) {
        switch (college) {
            case HUMANITIES:
                return 12; // 인문대 마지막 학과 val
            case SOCIAL_SCIENCE:
                return 19; // 사회과학대 마지막 학과 val
            case BUSINESS:
                return 23; // 경영대 마지막 학과 val
            case LAW:
                return 25; // 법대 마지막 학과 val
            case ICT:
                return 29; // ICT융합대 마지막 학과 val
            default:
                throw new IllegalArgumentException("해당 단과대는 존재하지 않습니다.");
        }
    }

    @JsonValue
    public String getAffiliationCode() {
        return this.councilName;
    }
}
