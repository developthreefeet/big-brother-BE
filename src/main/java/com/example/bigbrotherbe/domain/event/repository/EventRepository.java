package com.example.bigbrotherbe.domain.event.repository;

import com.example.bigbrotherbe.domain.event.entity.Event;
import com.example.bigbrotherbe.domain.meetings.entity.Meetings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findByAffiliationId(Long affiliationId, Pageable pageable);

    Page<Event> findByAffiliationIdAndTitleContaining(Long affiliationId, String title, Pageable pageable);
}
