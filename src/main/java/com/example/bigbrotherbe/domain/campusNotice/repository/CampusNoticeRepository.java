package com.example.bigbrotherbe.domain.campusNotice.repository;

import com.example.bigbrotherbe.domain.campusNotice.entity.CampusNotice;
import com.example.bigbrotherbe.domain.campusNotice.entity.CampusNoticeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampusNoticeRepository extends JpaRepository<CampusNotice, Long> {
    Page<CampusNotice> findByType(CampusNoticeType campusNoticeType, Pageable pageable);

    Page<CampusNotice> findByTypeAndTitleContaining(CampusNoticeType campusNoticeType, String title, Pageable pageable);
}
