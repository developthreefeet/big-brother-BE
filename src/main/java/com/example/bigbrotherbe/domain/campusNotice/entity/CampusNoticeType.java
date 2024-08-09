package com.example.bigbrotherbe.domain.campusNotice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CampusNoticeType {
    GENERAL_NOTICE("https://www.mju.ac.kr/mjukr/255/subview.do", 1),
    ACADEMIC_NOTICE("https://www.mju.ac.kr/mjukr/257/subview.do", 2);

    private final String url;
    private final int id;

    public static CampusNoticeType getTypeById(Long id){
        for (CampusNoticeType c : CampusNoticeType.values()){
            if (id == c.id){
                return c;
            }
        }
        return null;
    }
}