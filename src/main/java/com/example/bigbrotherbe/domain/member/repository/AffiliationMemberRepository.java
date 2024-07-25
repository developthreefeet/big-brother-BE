package com.example.bigbrotherbe.domain.member.repository;

import com.example.bigbrotherbe.domain.member.entity.role.AffiliationMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AffiliationMemberRepository extends JpaRepository<AffiliationMember, Long> {

}
