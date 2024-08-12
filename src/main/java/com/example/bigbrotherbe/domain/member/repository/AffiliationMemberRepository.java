package com.example.bigbrotherbe.domain.member.repository;

import com.example.bigbrotherbe.domain.member.entity.role.AffiliationMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AffiliationMemberRepository extends JpaRepository<AffiliationMember, Long> {

    List<AffiliationMember> findAllByMemberId(Long memberId);
}
