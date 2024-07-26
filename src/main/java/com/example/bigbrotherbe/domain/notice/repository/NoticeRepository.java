package com.example.bigbrotherbe.domain.notice.repository;

import com.example.bigbrotherbe.domain.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Optional<Notice> findByTitle(String noticeTitle);

    // 이름이 같은 공지를 찾을 이유가 있나??
    boolean existsByTitle(String noticeTitle);
}
