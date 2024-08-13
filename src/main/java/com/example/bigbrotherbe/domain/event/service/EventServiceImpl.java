package com.example.bigbrotherbe.domain.event.service;

import com.example.bigbrotherbe.domain.event.dto.request.EventRegisterRequest;
import com.example.bigbrotherbe.domain.event.dto.request.EventUpdateRequest;
import com.example.bigbrotherbe.domain.event.dto.response.EventResponse;
import com.example.bigbrotherbe.domain.event.entity.Event;
import com.example.bigbrotherbe.domain.event.repository.EventRepository;
import com.example.bigbrotherbe.domain.member.service.MemberService;
import com.example.bigbrotherbe.global.exception.BusinessException;
import com.example.bigbrotherbe.global.file.dto.FileDeleteDTO;
import com.example.bigbrotherbe.global.file.dto.FileSaveDTO;
import com.example.bigbrotherbe.global.file.dto.FileUpdateDTO;
import com.example.bigbrotherbe.global.file.entity.File;
import com.example.bigbrotherbe.global.file.enums.FileType;
import com.example.bigbrotherbe.global.file.service.FileService;
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

        List<File> files = null;
        if (fileService.checkExistRequestFile(multipartFiles)) {
            FileUpdateDTO fileUpdateDTO = FileUpdateDTO.builder()
                    .fileType(FileType.EVENT.getType())
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

        FileDeleteDTO fileDeleteDTO = FileDeleteDTO.builder()
                .fileType(FileType.EVENT.getType())
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

        List<String> urlList = event.getFiles().stream()
                .map(File::getUrl)
                .toList();

        return EventResponse.fromEventResponse(event, urlList);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Event> getEvents(Long affiliationId, Pageable pageable) {
        return eventRepository.findByAffiliationId(affiliationId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Event> searchEvent(Long affiliationId, String title, Pageable pageable) {
        return eventRepository.findByAffiliationIdAndTitleContaining(affiliationId, title, pageable);
    }
}
