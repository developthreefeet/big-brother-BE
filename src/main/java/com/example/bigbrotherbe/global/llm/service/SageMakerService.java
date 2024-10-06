package com.example.bigbrotherbe.global.llm.service;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sagemakerruntime.SageMakerRuntimeClient;
import software.amazon.awssdk.services.sagemakerruntime.model.InvokeEndpointRequest;
import software.amazon.awssdk.services.sagemakerruntime.model.InvokeEndpointResponse;
import software.amazon.awssdk.core.SdkBytes;
import org.springframework.stereotype.Service;

@Service 
@RequiredArgsConstructor

public class SageMakerService {

    private final SageMakerRuntimeClient sageMakerClient;

    public String getSummary(String inputText) {
        // 요청 생성
        // SageMaker 엔드포인트 이름
        String endpointName = "huggingface-bart-endpoint";
        InvokeEndpointRequest request = InvokeEndpointRequest.builder()
                .endpointName(endpointName)
                .contentType("application/json")
                .body(SdkBytes.fromUtf8String("{\"inputs\": \"" + inputText + "\"}"))
                .build();
        // 엔드포인트 호출
        InvokeEndpointResponse response = sageMakerClient.invokeEndpoint(request);

        // 응답 결과 처리
        String result = new String(response.body().asByteArray());
        return parseSummaryFromResponse(result);
    }

    private String parseSummaryFromResponse(String response) {
        // 응답에서 요약 결과 추출 (JSON 파싱)
        return response;
    }
}