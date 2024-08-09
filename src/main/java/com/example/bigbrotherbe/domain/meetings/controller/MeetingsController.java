package com.example.bigbrotherbe.domain.meetings.controller;

import com.example.bigbrotherbe.domain.meetings.dto.request.MeetingsRegisterRequest;
import com.example.bigbrotherbe.domain.meetings.dto.request.MeetingsUpdateRequest;
import com.example.bigbrotherbe.domain.meetings.dto.response.MeetingsResponse;
import com.example.bigbrotherbe.domain.meetings.entity.Meetings;
import com.example.bigbrotherbe.domain.meetings.service.MeetingsService;

import com.example.bigbrotherbe.global.exception.response.ApiResponse;
import com.example.bigbrotherbe.global.ocr.dto.OcrDTO;
import com.example.bigbrotherbe.global.ocr.service.OcrService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.example.bigbrotherbe.global.constant.Constant.GetContent.PAGE_DEFAULT_VALUE;
import static com.example.bigbrotherbe.global.constant.Constant.GetContent.SIZE_DEFAULT_VALUE;
import static com.example.bigbrotherbe.global.exception.enums.SuccessCode.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/big-brother/meetings")
public class MeetingsController {

    private final MeetingsService meetingsService;
    private final OcrService ocrService;


    @PostMapping("/test")
    public ResponseEntity<ApiResponse<OcrDTO>> testOcr(@RequestPart(value = "file") MultipartFile multipartFile) throws IOException {


        OcrDTO ocrDTO = ocrService.extractText(multipartFile);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, ocrDTO));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> registerMeetings(@RequestPart(value = "meetingsRegisterRequest") MeetingsRegisterRequest meetingsRegisterRequest,
                                                              @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
        meetingsService.registerMeetings(meetingsRegisterRequest, multipartFiles);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS));
    }

    @PutMapping("/{meetingsId}")
    public ResponseEntity<ApiResponse<Void>> updateMeetings(@PathVariable("meetingsId") Long meetingsId,
                                                            @RequestPart(value = "meetingsUpdateRequest") MeetingsUpdateRequest meetingsUpdateRequest,
                                                            @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
        meetingsService.updateMeetings(meetingsId, meetingsUpdateRequest, multipartFiles);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS));
    }

    @DeleteMapping("/{meetingsId}")
    public ResponseEntity<ApiResponse<Void>> deleteMeetings(@PathVariable("meetingsId") Long meetingsId) {
        meetingsService.deleteMeetings(meetingsId);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS));
    }

    @GetMapping("/{meetingsId}")
    public ResponseEntity<ApiResponse<MeetingsResponse>> getMeetingsById(@PathVariable("meetingsId") Long MeetingsId) {
        MeetingsResponse meetingsResponse = meetingsService.getMeetingsById(MeetingsId);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, meetingsResponse));
    }

    @GetMapping("all/{affiliationId}")
    public ResponseEntity<ApiResponse<Page<Meetings>>> getMeetingsList(@PathVariable("affiliationId") Long affiliationId,
                                                                       @RequestParam(name = "page", defaultValue = PAGE_DEFAULT_VALUE) int page,
                                                                       @RequestParam(name = "size", defaultValue = SIZE_DEFAULT_VALUE) int size,
                                                                       @RequestParam(name = "search", required = false) String search) {
        Page<Meetings> meetingsPage;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        if (search != null && !search.isEmpty()) {
            meetingsPage = meetingsService.searchMeetings(affiliationId, search, pageable);
        } else {
            meetingsPage = meetingsService.getMeetings(affiliationId, pageable);
        }
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, meetingsPage));
    }
}
