package com.example.bigbrotherbe.domain.campusNotice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CampusNoticeType {
    GENERAL_NOTICE("https://www.mju.ac.kr/mjukr/255/subview.do", "일반공지"),
    ACADEMIC_NOTICE("https://www.mju.ac.kr/mjukr/257/subview.do", "학사공지");

    private final String url;
    private final String name;

    public static CampusNoticeType getTypeByName(String name){
        for (CampusNoticeType c : CampusNoticeType.values()){
            if (name.equals(c.name)){
                return c;
            }
        }
        return null;
    }
}