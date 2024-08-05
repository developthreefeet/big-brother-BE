package com.example.bigbrotherbe.domain.event.repository;

import com.example.bigbrotherbe.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
