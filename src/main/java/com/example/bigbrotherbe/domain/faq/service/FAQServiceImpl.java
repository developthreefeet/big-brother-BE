package com.example.bigbrotherbe.domain.faq.service;

import com.example.bigbrotherbe.domain.affiliation.service.AffiliationService;
import com.example.bigbrotherbe.domain.faq.dto.request.FAQModifyRequest;
import com.example.bigbrotherbe.domain.faq.dto.request.FAQRegisterRequest;
import com.example.bigbrotherbe.domain.faq.dto.response.FAQResponse;
import com.example.bigbrotherbe.domain.faq.entity.FAQ;
import com.example.bigbrotherbe.domain.faq.repository.FAQRepository;
import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.global.exception.BusinessException;
import com.example.bigbrotherbe.global.exception.enums.ErrorCode;
import com.example.bigbrotherbe.global.file.dto.FileResponse;
import com.example.bigbrotherbe.global.file.dto.FileSaveDTO;
import com.example.bigbrotherbe.global.file.dto.FileUpdateDTO;
import com.example.bigbrotherbe.global.file.entity.File;
import com.example.bigbrotherbe.global.file.enums.FileType;
import com.example.bigbrotherbe.global.file.service.FileService;
import com.example.bigbrotherbe.global.jwt.component.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.bigbrotherbe.global.exception.enums.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class FAQServiceImpl implements FAQService {
    private final FAQRepository faqRepository;

    private final FileService fileService;
    private final AffiliationService affiliationService;

    private final AuthUtil authUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)   // 트랜잭션 시작 및 커밋 -> 모든 예외상황 발생시 롤백
    public void register(FAQRegisterRequest faqRegisterRequest, List<MultipartFile> multipartFiles) {
        if (!affiliationService.checkExistAffiliationById(faqRegisterRequest.getAffiliationId())) {
            throw new BusinessException(NO_EXIST_AFFILIATION);
        }

        if (authUtil.checkCouncilRole(faqRegisterRequest.getAffiliationId())) {
            throw new BusinessException(NOT_COUNCIL_MEMBER);
        }

        List<File> files = null;
        if (fileService.checkExistRequestFile(multipartFiles)) {
            FileSaveDTO fileSaveDTO = FileSaveDTO.builder()
                    .fileType(FileType.FAQ.getType())
                    .multipartFileList(multipartFiles)
                    .build();

            files = fileService.saveFiles(fileSaveDTO);
        }
        FAQ faq = faqRegisterRequest.toFAQEntity(files);

        if (files != null) {
            files.forEach(file -> {
                file.linkFAQ(faq);
            });
        }

        faqRepository.save(faq);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(Long faqId, FAQModifyRequest faqModifyRequest, List<MultipartFile> multipartFiles) {
        FAQ faq = faqRepository.findById(faqId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_EXIST_FAQ));

        if (authUtil.checkCouncilRole(faq.getAffiliationId())) {
            throw new BusinessException(NOT_COUNCIL_MEMBER);
        }

        List<File> files = null;
        if (fileService.checkExistRequestFile(multipartFiles)) {
            FileUpdateDTO fileUpdateDTO = FileUpdateDTO.builder()
                    .fileType(FileType.FAQ.getType())
                    .multipartFileList(multipartFiles)
                    .files(faq.getFiles())
                    .build();

            files = fileService.updateFile(fileUpdateDTO);
        }

        faq.update(faqModifyRequest.getTitle(), faqModifyRequest.getContent(), files);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long faqId) {
        FAQ faq = faqRepository.findById(faqId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_EXIST_FAQ));

        if (authUtil.checkCouncilRole(faq.getAffiliationId())) {
            throw new BusinessException(NOT_COUNCIL_MEMBER);
        }

        faqRepository.delete(faq);
    }

    @Override
    @Transactional(readOnly = true)
    public FAQResponse getFAQById(Long faqId) {
        FAQ faq = faqRepository.findById(faqId)
                .orElseThrow(() -> new BusinessException(NO_EXIST_FAQ));

        List<FileResponse> fileInfo = faq.getFiles().stream()
                .map(file -> FileResponse.of(file.getFileName(), file.getUrl()))
                .toList();

        return FAQResponse.fromFAQResponse(faq, fileInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FAQ> getFAQ(String affiliation, Pageable pageable) {
        Member member = authUtil.getLoginMember();
        Long affiliationId = authUtil.getAffiliationIdByMemberId(member.getId(), affiliation);
        return faqRepository.findByAffiliationId(affiliationId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FAQ> searchFAQ(String affiliation, String title, Pageable pageable) {
        Member member = authUtil.getLoginMember();
        Long affiliationId = authUtil.getAffiliationIdByMemberId(member.getId(), affiliation);
        return faqRepository.findByAffiliationIdAndTitleContaining(affiliationId, title, pageable);
    }
}
