package com.example.bigbrotherbe.domain.affiliation.component;


import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.domain.member.entity.enums.Role;
import com.example.bigbrotherbe.domain.member.entity.role.Affiliation;
import com.example.bigbrotherbe.domain.member.entity.role.AffiliationMember;
import com.example.bigbrotherbe.domain.member.repository.AffiliationMemberRepository;
import com.example.bigbrotherbe.domain.member.repository.AffiliationRepository;
import com.example.bigbrotherbe.global.exception.BusinessException;
import com.example.bigbrotherbe.global.exception.enums.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AffiliationManger {
    private final AffiliationRepository affiliationRepository;
    private final AffiliationMemberRepository affiliationMemberRepository;

    public Affiliation findAffiliationByName(String name) {
        return affiliationRepository.findByName(name)
            .orElseThrow(() -> new BusinessException(ErrorCode.NO_EXIST_AFFILIATION));
    }

    public AffiliationMember createAfiiliationMember(Member savedMember, String affiliationName, Role role) {
        Affiliation affiliation =  findAffiliationByName(affiliationName);
        AffiliationMember affiliationMember= AffiliationMember.builder()
            .member(savedMember)
            .affiliation(affiliation)
            .role(role.getRole())
            .build();
        return saveAffiliationMember(affiliationMember);
    }

    public AffiliationMember saveAffiliationMember(AffiliationMember affiliationMember) {
        return affiliationMemberRepository.save(affiliationMember);
    }

    public List<AffiliationMember> findAllByMemberId(Long id) {
        return affiliationMemberRepository.findAllByMemberId(id);

    }

    public boolean existsById(Long affiliationId) {
        if (!affiliationRepository.existsById(affiliationId)) {
            throw new BusinessException(ErrorCode.NO_EXIST_AFFILIATION);
        }
        return true;
    }
}
