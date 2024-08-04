package com.example.bigbrotherbe.domain.meetings.service;

import com.example.bigbrotherbe.domain.meetings.dto.MeetingsRegisterRequest;
import com.example.bigbrotherbe.domain.meetings.dto.MeetingsUpdateRequest;
import com.example.bigbrotherbe.domain.meetings.entity.Meetings;
import com.example.bigbrotherbe.domain.meetings.repository.MeetingsRepository;
import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.domain.member.service.MemberService;
import com.example.bigbrotherbe.global.exception.BusinessException;
import com.example.bigbrotherbe.global.file.dto.FileDeleteDTO;
import com.example.bigbrotherbe.global.file.dto.FileSaveDTO;
import com.example.bigbrotherbe.global.file.dto.FileUpdateDTO;
import com.example.bigbrotherbe.global.file.entity.File;
import com.example.bigbrotherbe.global.file.enums.FileType;
import com.example.bigbrotherbe.global.file.service.FileService;
import com.example.bigbrotherbe.global.jwt.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.bigbrotherbe.global.exception.enums.ErrorCode.NO_EXIST_AFFILIATION;
import static com.example.bigbrotherbe.global.exception.enums.ErrorCode.NO_EXIST_MEETINGS;

@Service
@RequiredArgsConstructor
public class MeetingsServiceImpl implements MeetingsService {

    private final MeetingsRepository meetingsRepository;

    private final FileService fileService;
    private final MemberService memberService;

    private final AuthUtil authUtil;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerMeetings(MeetingsRegisterRequest meetingsRegisterRequest, List<MultipartFile> multipartFiles) {
        if (!memberService.checkExistAffiliationById(meetingsRegisterRequest.getAffiliationId())) {
            throw new BusinessException(NO_EXIST_AFFILIATION);
        }

//                Member member = authUtil.getLoginMember();
        // role에 따라 권한있는지 필터링 없으면 exception

        List<File> files = null;
        if (fileService.checkExistRequestFile(multipartFiles)) {
            FileSaveDTO fileSaveDTO = FileSaveDTO.builder()
                    .fileType(FileType.MEETINGS.getType())
                    .multipartFileList(multipartFiles)
                    .build();

            files = fileService.saveFile(fileSaveDTO);
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

        List<File> files = null;
        if (fileService.checkExistRequestFile(multipartFiles)) {
            FileUpdateDTO fileUpdateDTO = FileUpdateDTO.builder()
                    .fileType(FileType.MEETINGS.getType())
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
                .fileType(FileType.MEETINGS.getType())
                .files(meetings.getFiles())
                .build();

        fileService.deleteFile(fileDeleteDTO);
        meetingsRepository.delete(meetings);
    }
}
