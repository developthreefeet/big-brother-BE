package com.example.bigbrotherbe.domain.affiliation.service;

import com.example.bigbrotherbe.domain.member.dto.response.AffiliationResponse;
import java.util.List;

public interface AffiliationService {
    boolean checkExistAffiliationById(Long affiliationId);

    List<AffiliationResponse> getColleges();

    List<AffiliationResponse> getDepartments(String councilName);


}
