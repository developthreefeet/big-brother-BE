package com.example.bigbrotherbe.domain.campusNotice.service;

import com.example.bigbrotherbe.domain.campusNotice.dto.CampusNoticeResponse;
import com.example.bigbrotherbe.domain.campusNotice.entity.CampusNotice;
import com.example.bigbrotherbe.domain.campusNotice.entity.CampusNoticeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CampusNoticeService {
    void invokeLambda(String functionName, String payload, CampusNoticeType noticeType);

    CampusNoticeResponse getCampusNoticeById(Long campusNoticeId);

    Page<CampusNotice> getCampusNotice(CampusNoticeType campusNoticeType, Pageable pageable);

    Page<CampusNotice> searchCampusNotice(CampusNoticeType campusNoticeType, String title, Pageable pageable);
}
