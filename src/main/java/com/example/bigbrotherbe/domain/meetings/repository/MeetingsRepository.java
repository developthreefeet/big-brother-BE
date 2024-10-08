package com.example.bigbrotherbe.domain.meetings.repository;

import com.example.bigbrotherbe.domain.meetings.entity.Meetings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingsRepository extends JpaRepository<Meetings, Long> {
    Page<Meetings> findByAffiliationId(Long affiliationId, Pageable pageable);

    Page<Meetings> findByAffiliationIdAndTitleContaining(Long affiliationId, String title, Pageable pageable);
}
