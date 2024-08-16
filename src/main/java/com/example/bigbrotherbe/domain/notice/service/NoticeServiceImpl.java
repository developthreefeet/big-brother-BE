package com.example.bigbrotherbe.domain.notice.service;

import com.example.bigbrotherbe.domain.affiliation.service.AffiliationService;
import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.domain.notice.dto.response.NoticeResponse;
import com.example.bigbrotherbe.domain.notice.entity.Notice;
import com.example.bigbrotherbe.domain.notice.dto.request.NoticeModifyRequest;
import com.example.bigbrotherbe.domain.notice.dto.request.NoticeRegisterRequest;
import com.example.bigbrotherbe.domain.notice.repository.NoticeRepository;
import com.example.bigbrotherbe.global.exception.BusinessException;
import com.example.bigbrotherbe.global.exception.enums.ErrorCode;
import com.example.bigbrotherbe.global.file.dto.FileDeleteDTO;
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
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    private final FileService fileService;
    private final AffiliationService affiliationService;

    private final AuthUtil authUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)   // 트랜잭션 시작 및 커밋 -> 모든 예외상황 발생시 롤백
    public void register(NoticeRegisterRequest noticeRegisterRequest, List<MultipartFile> multipartFiles) {
        if (!affiliationService.checkExistAffiliationById(noticeRegisterRequest.getAffiliationId())) {
            throw new BusinessException(NO_EXIST_AFFILIATION);
        }

        if (authUtil.checkCouncilRole(noticeRegisterRequest.getAffiliationId())) {
            throw new BusinessException(NOT_COUNCIL_MEMBER);
        }

        List<File> files = null;
        if (fileService.checkExistRequestFile(multipartFiles)) {
            FileSaveDTO fileSaveDTO = FileSaveDTO.builder()
                    .fileType(FileType.NOTICE.getType())
                    .multipartFileList(multipartFiles)
                    .build();

            files = fileService.saveFiles(fileSaveDTO);
        }
        Notice notice = noticeRegisterRequest.toNoticeEntity(files);

        if (files != null) {
            files.forEach(file -> {
                file.linkNotice(notice);
            });
        }

        noticeRepository.save(notice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(Long noticeId, NoticeModifyRequest noticeModifyRequest, List<MultipartFile> multipartFiles) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_EXIST_NOTICE));

        if (authUtil.checkCouncilRole(notice.getAffiliationId())) {
            throw new BusinessException(NOT_COUNCIL_MEMBER);
        }

        List<File> files = null;
        if (fileService.checkExistRequestFile(multipartFiles)) {
            FileUpdateDTO fileUpdateDTO = FileUpdateDTO.builder()
                    .fileType(FileType.NOTICE.getType())
                    .multipartFileList(multipartFiles)
                    .files(notice.getFiles())
                    .build();

            files = fileService.updateFile(fileUpdateDTO);
        }

        notice.update(noticeModifyRequest.getTitle(), noticeModifyRequest.getContent(), files);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_EXIST_NOTICE));

        if (authUtil.checkCouncilRole(notice.getAffiliationId())) {
            throw new BusinessException(NOT_COUNCIL_MEMBER);
        }

        FileDeleteDTO fileDeleteDTO = FileDeleteDTO.builder()
                .fileType(FileType.NOTICE.getType())
                .files(notice.getFiles())
                .build();

        fileService.deleteFile(fileDeleteDTO);
        noticeRepository.delete(notice);
    }

    @Override
    public NoticeResponse getNoticeById(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new BusinessException(NO_EXIST_NOTICE));

        List<String> urlList = notice.getFiles().stream()
                .map(File::getUrl)
                .toList();

        return NoticeResponse.fromNoticeResponse(notice, urlList);
    }

    @Override
    public Page<Notice> getNotice(String affiliation, Pageable pageable) {
        Member member = authUtil.getLoginMember();
        Long affiliationId = authUtil.getAffiliationIdByMemberId(member.getId(), affiliation);
        return noticeRepository.findByAffiliationId(affiliationId, pageable);
    }

    @Override
    public Page<Notice> searchNotice(String affiliation, String title, Pageable pageable) {
        Member member = authUtil.getLoginMember();
        Long affiliationId = authUtil.getAffiliationIdByMemberId(member.getId(), affiliation);
        return noticeRepository.findByAffiliationIdAndTitleContaining(affiliationId, title, pageable);
    }
}
