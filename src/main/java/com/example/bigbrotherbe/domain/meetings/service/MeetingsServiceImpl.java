package com.example.bigbrotherbe.domain.meetings.service;

import com.example.bigbrotherbe.domain.meetings.dto.MeetingsRegisterRequest;
import com.example.bigbrotherbe.domain.meetings.dto.MeetingsUpdateRequest;
import com.example.bigbrotherbe.domain.meetings.entity.Meetings;
import com.example.bigbrotherbe.domain.meetings.repository.MeetingsRepository;
import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.global.file.dto.FileSaveDTO;
import com.example.bigbrotherbe.global.file.entity.File;
import com.example.bigbrotherbe.global.file.enums.FileType;
import com.example.bigbrotherbe.global.file.service.FileService;
import com.example.bigbrotherbe.global.jwt.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MeetingsServiceImpl implements MeetingsService {

    private final MeetingsRepository meetingsRepository;

    private final AuthUtil authUtil;
    private final FileService fileService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerMeetings(MeetingsRegisterRequest meetingsRegisterRequest, List<MultipartFile> multipartFiles) {

        // 해당 소속이 있는지 필터링 affiliation _id 없으면 exception
        // role에 따라 권한있는지 필터링 없으면 exception

        Member member = authUtil.getLoginMember();

        FileSaveDTO fileSaveDTO = FileSaveDTO.builder()
                .fileType(FileType.MEETINGS.getType())
                .multipartFileList(multipartFiles)
                .build();

        List<File> files = fileService.saveFile(fileSaveDTO);

        meetingsRepository.save(meetingsRegisterRequest.toMeetingsEntity(files));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMeetings(Long meetingsId, MeetingsUpdateRequest meetingsUpdateRequest) {
        Meetings meetings = meetingsRepository.findById(meetingsId)
                .orElseThrow(() -> new NoSuchElementException("없어요~~"));

        meetings.update(meetingsUpdateRequest.getTitle(), meetingsUpdateRequest.getContent(), meetingsUpdateRequest.isPublic());
    }
}
