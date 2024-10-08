package com.example.bigbrotherbe.domain.meetings.service;

import com.example.bigbrotherbe.domain.affiliation.service.AffiliationService;
import com.example.bigbrotherbe.domain.meetings.dto.request.MeetingsRegisterRequest;
import com.example.bigbrotherbe.domain.meetings.dto.request.MeetingsUpdateRequest;
import com.example.bigbrotherbe.domain.meetings.dto.response.MeetingsResponse;
import com.example.bigbrotherbe.domain.meetings.entity.Meetings;
import com.example.bigbrotherbe.domain.meetings.repository.MeetingsRepository;
import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.global.common.exception.BusinessException;
import com.example.bigbrotherbe.global.file.dto.FileDeleteDTO;
import com.example.bigbrotherbe.global.file.dto.FileResponse;
import com.example.bigbrotherbe.global.file.dto.FileSaveDTO;
import com.example.bigbrotherbe.global.file.dto.FileUpdateDTO;
import com.example.bigbrotherbe.global.file.entity.File;
import com.example.bigbrotherbe.global.common.enums.EntityType;
import com.example.bigbrotherbe.global.file.service.FileService;
import com.example.bigbrotherbe.global.file.util.FileUtil;
import com.example.bigbrotherbe.global.auth.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.bigbrotherbe.global.common.exception.enums.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MeetingsServiceImpl implements MeetingsService {

    private final MeetingsRepository meetingsRepository;

    private final FileService fileService;
    private final AffiliationService affiliationService;

    private final AuthUtil authUtil;
    private final FileUtil fileUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerMeetings(MeetingsRegisterRequest meetingsRegisterRequest, List<MultipartFile> multipartFiles) {
        if (!affiliationService.checkExistAffiliationById(meetingsRegisterRequest.getAffiliationId())) {
            throw new BusinessException(NO_EXIST_AFFILIATION);
        }
        if (authUtil.checkCouncilRole(meetingsRegisterRequest.getAffiliationId())) {
            throw new BusinessException(NOT_COUNCIL_MEMBER);
        }

        fileUtil.checkPdfFiles(multipartFiles);

        List<File> files = null;
        if (fileService.checkExistRequestFile(multipartFiles)) {
            FileSaveDTO fileSaveDTO = FileSaveDTO.builder()
                    .fileType(EntityType.MEETINGS_TYPE.getType())
                    .multipartFileList(multipartFiles)
                    .build();

            files = fileService.saveFiles(fileSaveDTO);
        }
        Meetings meetings = meetingsRegisterRequest.toMeetingsEntity(files);

        if (files != null) {
            files.forEach(file -> {
                file.linkMeeting(meetings);
            });
        }

        meetingsRepository.save(meetings);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMeetings(Long meetingsId, MeetingsUpdateRequest meetingsUpdateRequest, List<MultipartFile> multipartFiles) {
        Meetings meetings = meetingsRepository.findById(meetingsId)
                .orElseThrow(() -> new BusinessException(NO_EXIST_MEETINGS));

        if (authUtil.checkCouncilRole(meetings.getAffiliationId())) {
            throw new BusinessException(NOT_COUNCIL_MEMBER);
        }

        fileUtil.checkPdfFiles(multipartFiles);

        List<File> files = null;
        if (fileService.checkExistRequestFile(multipartFiles)) {
            FileUpdateDTO fileUpdateDTO = FileUpdateDTO.builder()
                    .fileType(EntityType.MEETINGS_TYPE.getType())
                    .multipartFileList(multipartFiles)
                    .files(meetings.getFiles())
                    .build();

            files = fileService.updateFile(fileUpdateDTO);
        }

        meetings.update(meetingsUpdateRequest.getTitle(), meetingsUpdateRequest.getContent(), meetingsUpdateRequest.isPublic(), files);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMeetings(Long meetingsId) {
        Meetings meetings = meetingsRepository.findById(meetingsId)
                .orElseThrow(() -> new BusinessException(NO_EXIST_MEETINGS));

        FileDeleteDTO fileDeleteDTO = FileDeleteDTO.builder()
                .fileType(EntityType.MEETINGS_TYPE.getType())
                .files(meetings.getFiles())
                .build();

        fileService.deleteFile(fileDeleteDTO);
        meetingsRepository.delete(meetings);
    }

    @Override
    @Transactional(readOnly = true)
    public MeetingsResponse getMeetingsById(Long meetingsId) {
        Meetings meetings = meetingsRepository.findById(meetingsId)
                .orElseThrow(() -> new BusinessException(NO_EXIST_MEETINGS));

        List<FileResponse> fileInfo = meetings.getFiles().stream()
                .map(file -> FileResponse.of(file.getFileName(), file.getUrl()))
                .toList();

        return MeetingsResponse.fromMeetingsResponse(meetings, fileInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Meetings> getMeetings(String affiliation, Pageable pageable) {
        Member member = authUtil.getLoginMember();
        Long affiliationId = authUtil.getAffiliationIdByMemberId(member.getId(), affiliation);
        return meetingsRepository.findByAffiliationId(affiliationId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Meetings> searchMeetings(String affiliation, String title, Pageable pageable) {
        Member member = authUtil.getLoginMember();
        Long affiliationId = authUtil.getAffiliationIdByMemberId(member.getId(), affiliation);
        return meetingsRepository.findByAffiliationIdAndTitleContaining(affiliationId, title, pageable);
    }

}
