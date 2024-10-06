package com.example.bigbrotherbe.global.llm.controller;
import com.example.bigbrotherbe.global.llm.service.SageMakerService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/v1/admin/llm")
public class SummarizationController {

    @Autowired
    private SageMakerService sageMakerService;

    @GetMapping()
    public String summarize(@RequestPart(value = "text") String inputText) {
        return sageMakerService.getSummary(inputText);
    }
}