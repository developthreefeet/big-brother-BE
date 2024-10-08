package com.example.bigbrotherbe.domain.event.service;

import com.example.bigbrotherbe.domain.affiliation.service.AffiliationService;
import com.example.bigbrotherbe.domain.event.dto.request.EventRegisterRequest;
import com.example.bigbrotherbe.domain.event.dto.request.EventUpdateRequest;
import com.example.bigbrotherbe.domain.event.dto.response.EventResponse;
import com.example.bigbrotherbe.domain.event.entity.Event;
import com.example.bigbrotherbe.domain.event.repository.EventRepository;
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
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final AffiliationService affiliationService;
    private final FileService fileService;

    private final FileUtil fileUtil;
    private final AuthUtil authUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerEvent(EventRegisterRequest eventRegisterRequest, List<MultipartFile> multipartFiles) {
        if (!affiliationService.checkExistAffiliationById(eventRegisterRequest.getAffiliationId())) {
            throw new BusinessException(NO_EXIST_AFFILIATION);
        }

        if (authUtil.checkCouncilRole(eventRegisterRequest.getAffiliationId())) {
            throw new BusinessException(NOT_COUNCIL_MEMBER);
        }

        fileUtil.CheckImageFiles(multipartFiles);

        List<File> files = null;
        if (fileService.checkExistRequestFile(multipartFiles)) {
            FileSaveDTO fileSaveDTO = FileSaveDTO.builder()
                    .fileType(EntityType.EVENT_TYPE.getType())
                    .multipartFileList(multipartFiles)
                    .build();

            files = fileService.saveFiles(fileSaveDTO);
        }
        Event event = eventRegisterRequest.toEventEntity(files);

        if (files != null) {
            files.forEach(file -> {
                file.linkEvent(event);
            });
        }

        eventRepository.save(event);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEvent(Long eventId, EventUpdateRequest eventUpdateRequest, List<MultipartFile> multipartFiles) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new BusinessException(NO_EXIST_EVENT));

        if (authUtil.checkCouncilRole(event.getAffiliationId())) {
            throw new BusinessException(NOT_COUNCIL_MEMBER);
        }

        fileUtil.CheckImageFiles(multipartFiles);

        List<File> files = null;
        if (fileService.checkExistRequestFile(multipartFiles)) {
            FileUpdateDTO fileUpdateDTO = FileUpdateDTO.builder()
                    .fileType(EntityType.EVENT_TYPE.getType())
                    .multipartFileList(multipartFiles)
                    .files(event.getFiles())
                    .build();

            files = fileService.updateFile(fileUpdateDTO);
        }

        event.update(eventUpdateRequest.getTitle(),
                eventUpdateRequest.getContent(),
                eventUpdateRequest.getTarget(),
                eventUpdateRequest.getStartDateTime(),
                eventUpdateRequest.getStartDateTime(),
                files);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new BusinessException(NO_EXIST_EVENT));

        if (authUtil.checkCouncilRole(event.getAffiliationId())) {
            throw new BusinessException(NOT_COUNCIL_MEMBER);
        }

        FileDeleteDTO fileDeleteDTO = FileDeleteDTO.builder()
                .fileType(EntityType.EVENT_TYPE.getType())
                .files(event.getFiles())
                .build();

        fileService.deleteFile(fileDeleteDTO);
        eventRepository.delete(event);
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponse getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new BusinessException(NO_EXIST_EVENT));

        List<FileResponse> fileInfo = event.getFiles().stream()
                .map(file -> FileResponse.of(file.getFileName(), file.getUrl()))
                .toList();

        return EventResponse.fromEventResponse(event, fileInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Event> getEvents(String affiliation, Pageable pageable) {
        Member member = authUtil.getLoginMember();
        Long affiliationId = authUtil.getAffiliationIdByMemberId(member.getId(), affiliation);
        return eventRepository.findByAffiliationId(affiliationId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Event> searchEvent(String affiliation, String title, Pageable pageable) {
        Member member = authUtil.getLoginMember();
        Long affiliationId = authUtil.getAffiliationIdByMemberId(member.getId(), affiliation);
        return eventRepository.findByAffiliationIdAndTitleContaining(affiliationId, title, pageable);
    }
}
