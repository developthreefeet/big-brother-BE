package com.example.bigbrotherbe.domain.campusNotice.service;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.example.bigbrotherbe.domain.campusNotice.dto.CampusNoticeRegisterRequest;
import com.example.bigbrotherbe.domain.campusNotice.dto.CampusNoticeResponse;
import com.example.bigbrotherbe.domain.campusNotice.entity.CampusNotice;
import com.example.bigbrotherbe.domain.campusNotice.entity.CampusNoticeType;
import com.example.bigbrotherbe.domain.campusNotice.repository.CampusNoticeRepository;
import com.example.bigbrotherbe.common.exception.BusinessException;
import com.example.bigbrotherbe.file.dto.FileResponse;
import com.example.bigbrotherbe.file.service.FileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CampusNoticeServiceImpl implements CampusNoticeService {

    private final CampusNoticeRepository campusNoticeRepository;

    private final AWSLambda awsLambda;
    private final ObjectMapper objectMapper;
    private final FileService fileService;

    @Transactional(rollbackFor = Exception.class)
    public void invokeLambda(String functionName, String payload, CampusNoticeType noticeType) {
        InvokeRequest invokeRequest = new InvokeRequest()
                .withFunctionName(functionName)
                .withPayload(payload);

        InvokeResult invokeResult = awsLambda.invoke(invokeRequest);
        String jsonResponse = new String(invokeResult.getPayload().array());
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            String body = root.get("body").asText();

            CampusNoticeRegisterRequest[] campusNoticeRegisterRequests =
                    objectMapper.readValue(body, CampusNoticeRegisterRequest[].class);
            List<CampusNotice> campusNotices = new ArrayList<>();
            for (CampusNoticeRegisterRequest campusNoticeRegisterRequest : campusNoticeRegisterRequests) {
                CampusNotice campusNotice = campusNoticeRegisterRequest.toCampusNoticeEntity(fileService, noticeType);
                campusNotices.add(campusNotice);
            }
            this.campusNoticeRepository.saveAll(campusNotices);
        } catch (JsonProcessingException e) {
            throw new BusinessException(FAIL_TO_JSON_PARSING);
        } catch (NullPointerException e) {
            throw new BusinessException(LAMBDA_FUNCTION_ERROR);
        }
    }

    @Override
    public CampusNoticeResponse getCampusNoticeById(Long campusNoticeId) {
        CampusNotice campusNotice = campusNoticeRepository.findById(campusNoticeId)
                .orElseThrow(() -> new BusinessException(NO_EXIST_CAMPUS_NOTICE));

        List<FileResponse> fileInfo = campusNotice.getFiles().stream()
                .map(file -> FileResponse.of(file.getFileName(), file.getUrl()))
                .toList();

        return CampusNoticeResponse.fromCampusNoticeResponse(campusNotice, fileInfo);
    }

    @Override
    public Page<CampusNotice> getCampusNotice(CampusNoticeType campusNoticeType, Pageable pageable) {
        return campusNoticeRepository.findByType(campusNoticeType, pageable);
    }

    @Override
    public Page<CampusNotice> searchCampusNotice(CampusNoticeType campusNoticeType, String title, Pageable pageable) {
        return campusNoticeRepository.findByTypeAndTitleContaining(campusNoticeType, title, pageable);
    }
}
