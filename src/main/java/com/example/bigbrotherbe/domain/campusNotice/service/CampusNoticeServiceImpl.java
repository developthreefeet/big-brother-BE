package com.example.bigbrotherbe.domain.campusNotice.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.example.bigbrotherbe.domain.campusNotice.dto.CampusNoticeResponse;
import com.example.bigbrotherbe.domain.campusNotice.entity.CampusNotice;
import com.example.bigbrotherbe.domain.campusNotice.entity.CampusNoticeType;
import com.example.bigbrotherbe.domain.campusNotice.repository.CampusNoticeRepository;
import com.example.bigbrotherbe.global.exception.BusinessException;
import com.example.bigbrotherbe.global.file.entity.File;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static com.example.bigbrotherbe.global.exception.enums.ErrorCode.*;

@Service
public class CampusNoticeServiceImpl implements CampusNoticeService {

    private final CampusNoticeRepository campusNoticeRepository;
    private final AWSLambda awsLambda;
    private final ObjectMapper objectMapper;

    public CampusNoticeServiceImpl(@Value("${cloud.aws.credentials.access-key}") String accessKeyId,
                                   @Value("${cloud.aws.credentials.secret-key}") String secretKey,
                                   @Value("${cloud.aws.region.static}") String region,
                                   CampusNoticeRepository campusNoticeRepository) {

        this.campusNoticeRepository = campusNoticeRepository;

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKeyId, secretKey);

        this.awsLambda = AWSLambdaClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    public void invokeLambda(String functionName, String payload, CampusNoticeType noticeType) {
        InvokeRequest invokeRequest = new InvokeRequest()
                .withFunctionName(functionName)
                .withPayload(payload);

        InvokeResult invokeResult = awsLambda.invoke(invokeRequest);
        String jsonResponse = new String(invokeResult.getPayload().array());
        System.out.println(jsonResponse);
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            String body = root.get("body").asText();

            List<CampusNotice> campusNotices = Arrays.asList(objectMapper.readValue(body, CampusNotice[].class));
            for (CampusNotice c : campusNotices) {
                c.setType(noticeType);
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

        List<String> urlList = campusNotice.getFiles().stream()
                .map(File::getUrl)
                .toList();

        return CampusNoticeResponse.fromCampusNoticeResponse(campusNotice, urlList);
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
