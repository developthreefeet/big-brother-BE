package com.example.bigbrotherbe.domain.faq.service;

import com.example.bigbrotherbe.domain.faq.dto.FAQModifyRequest;
import com.example.bigbrotherbe.domain.faq.dto.FAQRegisterRequest;
import com.example.bigbrotherbe.domain.faq.entity.FAQ;
import com.example.bigbrotherbe.domain.faq.repository.FAQRepository;
import com.example.bigbrotherbe.domain.member.service.MemberService;
import com.example.bigbrotherbe.global.exception.BusinessException;
import com.example.bigbrotherbe.global.exception.enums.ErrorCode;
import com.example.bigbrotherbe.global.file.dto.FileSaveDTO;
import com.example.bigbrotherbe.global.file.entity.File;
import com.example.bigbrotherbe.global.file.enums.FileType;
import com.example.bigbrotherbe.global.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.bigbrotherbe.global.exception.enums.ErrorCode.NO_EXIST_AFFILIATION;
@Service
@RequiredArgsConstructor
public class FAQServiceImpl implements FAQService{
    private final FAQRepository faqRepository;

    private final FileService fileService;
    private final MemberService memberService;

    @Override
    @Transactional(rollbackFor = Exception.class)   // 트랜잭션 시작 및 커밋 -> 모든 예외상황 발생시 롤백
    public void register(FAQRegisterRequest faqRegisterRequest, List<MultipartFile> multipartFiles) {
        if (!memberService.checkExistAffiliationById(faqRegisterRequest.getAffiliationId())) {
            throw new BusinessException(NO_EXIST_AFFILIATION);
        }

        //        Member member = authUtil.getLoginMember();
        // role에 따라 권한있는지 필터링 없으면 exception

        List<File> files = null;
        if (fileService.checkExistRequestFile(multipartFiles)) {
            FileSaveDTO fileSaveDTO = FileSaveDTO.builder()
                    .fileType(FileType.MEETINGS.getType())
                    .multipartFileList(multipartFiles)
                    .build();

            files = fileService.saveFile(fileSaveDTO);
        }
        faqRepository.save(faqRegisterRequest.toFAQEntity(files));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(Long faqId, FAQModifyRequest faqModifyRequest) {
        FAQ faq = faqRepository.findById(faqId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_EXIST_FAQ));

        faq.update(faqModifyRequest.getTitle(), faqModifyRequest.getContent());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long faqId) {
        FAQ faq = faqRepository.findById(faqId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_EXIST_FAQ));


        faqRepository.delete(faq);
    }
}
