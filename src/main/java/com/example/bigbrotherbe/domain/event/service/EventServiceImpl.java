package com.example.bigbrotherbe.domain.event.service;

import com.example.bigbrotherbe.domain.event.dto.request.EventRegisterRequest;
import com.example.bigbrotherbe.domain.event.entity.Event;
import com.example.bigbrotherbe.domain.event.repository.EventRepository;
import com.example.bigbrotherbe.domain.meetings.dto.request.MeetingsRegisterRequest;
import com.example.bigbrotherbe.domain.meetings.entity.Meetings;
import com.example.bigbrotherbe.domain.member.service.MemberService;
import com.example.bigbrotherbe.global.exception.BusinessException;
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
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final MemberService memberService;
    private final FileService fileService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerEvent(EventRegisterRequest eventRegisterRequest, List<MultipartFile> multipartFiles) {
        if (!memberService.checkExistAffiliationById(eventRegisterRequest.getAffiliationId())) {
            throw new BusinessException(NO_EXIST_AFFILIATION);
        }

        List<File> files = null;
        if (fileService.checkExistRequestFile(multipartFiles)) {
            FileSaveDTO fileSaveDTO = FileSaveDTO.builder()
                    .fileType(FileType.EVENT.getType())
                    .multipartFileList(multipartFiles)
                    .build();

            files = fileService.saveFile(fileSaveDTO);
        }
        Event event = eventRegisterRequest.toEventEntity(files);

        if (files != null) {
            files.forEach(file -> {
                file.linkEvent(event);
            });
        }

        eventRepository.save(event);
    }
}
