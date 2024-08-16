package com.example.bigbrotherbe.domain.affiliation.service;

import com.example.bigbrotherbe.domain.member.dto.response.AffiliationResponse;
import java.util.List;
import org.springframework.stereotype.Service;

public interface AffiliationService {
    boolean checkExistAffiliationById(Long affiliationId);

    List<AffiliationResponse> getColleges();

    List<AffiliationResponse> getDepartments(String councilName);


}
