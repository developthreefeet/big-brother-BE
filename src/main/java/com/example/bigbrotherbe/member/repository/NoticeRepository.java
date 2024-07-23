package com.example.bigbrotherbe.member.repository;

import com.example.bigbrotherbe.member.entity.Member;
import com.example.bigbrotherbe.member.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Member, Long> {
    Optional<Notice> findByNoticeTitle(String noticeTitle);

    boolean existsByNoticeTitle(String noticeTitle);
}
