package com.example.bigbrotherbe.domain.affiliation.service;

import com.example.bigbrotherbe.domain.affiliation.component.AffiliationManger;
import com.example.bigbrotherbe.domain.member.dto.response.AffiliationResponse;
import com.example.bigbrotherbe.domain.member.entity.enums.AffiliationCode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AffiliationServiceImpl implements AffiliationService {

    private final AffiliationManger affiliationManger;

    private final String CollegeKoreanText = "단과대";

    @Override
    public boolean checkExistAffiliationById(Long affiliationId) {
        return affiliationManger.existsById(affiliationId);
    }


    @Override
    public List<AffiliationResponse> getColleges() {
        return Arrays.stream(AffiliationCode.values())
                .filter(code -> code.getCouncilType().equals(CollegeKoreanText))
                .map(code -> AffiliationResponse.fromAffiliationResponse(code.getVal(), code.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<AffiliationResponse> getDepartments(String councilName) {
        return AffiliationCode.getDepartmentsByCollegeName(councilName);
    }
}
