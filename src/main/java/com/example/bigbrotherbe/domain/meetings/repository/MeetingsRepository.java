package com.example.bigbrotherbe.domain.meetings.repository;

import com.example.bigbrotherbe.domain.meetings.entity.Meetings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingsRepository  extends JpaRepository<Meetings, Long> {
}
