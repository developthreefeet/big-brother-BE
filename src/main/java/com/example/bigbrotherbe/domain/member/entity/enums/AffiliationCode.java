package com.example.bigbrotherbe.domain.member.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AffiliationCode {

    // 총학
    STUDENT_COUNCIL(1, "총학", "총학생회"),

    // 인문대
    HUMANITIES(2, "단과대", "인문대"),
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
    SOCIAL_SCIENCE(13, "단과대", "사회과학대"),
    PUBLIC_ADMINISTRATION(14, "학과", "행정학과"),
    ECONOMICS(15, "학과", "경제학과"),
    POLITICAL_SCIENCE_AND_DIPLOMACY(16, "학과", "정치외교학과"),
    DIGITAL_MEDIA(17, "학과", "디지털미디어학과"),
    CHILD_STUDIES(18, "학과", "아동학과"),
    YOUTH_GUIDANCE(19, "학과", "청소년지도학과"),

    // 경영대
    BUSINESS(20, "단과대", "경영대"),
    BUSINESS_ADMINISTRATION(21, "학과", "경영학과"),
    MANAGEMENT_INFORMATION_SYSTEMS(22, "학과", "경영정보학과"),
    INTERNATIONAL_TRADE(23, "학과", "국제통상학과"),

    // 법대
    LAW(24, "단과대", "법대"),
    DEPARTMENT_OF_LAW(25, "학과", "법학과"),

    // ICT융합대
    ICT(26, "단과대", "ICT융합대"),
    DIGITAL_CONTENTS_DESIGN(27, "학과", "디지털콘텐츠디자인학과"),
    SOFTWARE_APPLICATIONS(28, "학과", "응용소프트웨어전공"),
    DATA_TECHNOLOGY(29, "학과", "데이터테크놀로지전공");

    private final int val;
    private final String councilType;
    private final String councilName;
}
