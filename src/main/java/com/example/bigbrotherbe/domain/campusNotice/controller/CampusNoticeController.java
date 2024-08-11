package com.example.bigbrotherbe.domain.campusNotice.controller;

import com.example.bigbrotherbe.domain.campusNotice.dto.CampusNoticeResponse;
import com.example.bigbrotherbe.domain.campusNotice.entity.CampusNotice;
import com.example.bigbrotherbe.domain.campusNotice.entity.CampusNoticeType;
import com.example.bigbrotherbe.domain.campusNotice.service.CampusNoticeService;
import com.example.bigbrotherbe.global.exception.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.bigbrotherbe.global.constant.Constant.GetContent.PAGE_DEFAULT_VALUE;
import static com.example.bigbrotherbe.global.constant.Constant.GetContent.SIZE_DEFAULT_VALUE;
import static com.example.bigbrotherbe.global.exception.enums.SuccessCode.SUCCESS;

@RestController
@RequestMapping("/api/big-brother/campusnotice")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
public class CampusNoticeController {

    private final CampusNoticeService campusNoticeService;

    @GetMapping("/{campusNoticeId}")
    public ResponseEntity<ApiResponse<CampusNoticeResponse>> getCampusNoticeById(@PathVariable("campusNoticeId") Long campusNoticeId) {
        CampusNoticeResponse campusNoticeResponse = campusNoticeService.getCampusNoticeById(campusNoticeId);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, campusNoticeResponse));
    }

    @GetMapping("all/{campusNoticeTypeId}")
    public ResponseEntity<ApiResponse<Page<CampusNotice>>> getCampusNoticeList(@PathVariable("campusNoticeTypeId") Long campusNoticeTypeId,
                                                                       @RequestParam(name = "page", defaultValue = PAGE_DEFAULT_VALUE) int page,
                                                                       @RequestParam(name = "size", defaultValue = SIZE_DEFAULT_VALUE) int size,
                                                                       @RequestParam(name = "search", required = false) String search) {
        Page<CampusNotice> campusNoticePage;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        CampusNoticeType campusNoticeType = CampusNoticeType.getTypeById(campusNoticeTypeId);
        if (search != null && !search.isEmpty()) {
            campusNoticePage = campusNoticeService.searchCampusNotice(campusNoticeType, search, pageable);
        } else {
            campusNoticePage = campusNoticeService.getCampusNotice(campusNoticeType, pageable);
        }
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, campusNoticePage));
    }
}
