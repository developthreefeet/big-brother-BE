package com.example.bigbrotherbe.global.llm.service;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.example.bigbrotherbe.global.common.constant.Constant;
import com.example.bigbrotherbe.global.common.exception.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.bigbrotherbe.global.common.exception.enums.ErrorCode.FAIL_TO_JSON_PARSING;
import static com.example.bigbrotherbe.global.common.exception.enums.ErrorCode.LAMBDA_FUNCTION_ERROR;

@Service 
@RequiredArgsConstructor
public class BedrockServiceImpl implements BedrockService{

    private final AWSLambda awsLambda;
    private final ObjectMapper objectMapper;

    public String getSummary(String content) {
        String payload = this.createPayload(content);
        InvokeRequest invokeRequest = new InvokeRequest()
                .withFunctionName(Constant.LLM.FUNCTION_NAME)
                .withPayload(payload);

        InvokeResult invokeResult = awsLambda.invoke(invokeRequest);
        String jsonResponse = new String(invokeResult.getPayload().array());

        try{
            JsonNode root = objectMapper.readTree(jsonResponse);
            return root.get("body").asText();
        } catch (
            JsonProcessingException e) {
            throw new BusinessException(FAIL_TO_JSON_PARSING);
        } catch (NullPointerException e) {
            throw new BusinessException(LAMBDA_FUNCTION_ERROR);
        }
    }

    private String createPayload(String content) {
        return "{" +
                "\"content\": \"" + content + "\"" +
                "}";
    }

}