package com.example.bigbrotherbe.domain.campusNotice.dto;

import com.example.bigbrotherbe.domain.campusNotice.entity.CampusNotice;
import com.example.bigbrotherbe.domain.campusNotice.entity.CampusNoticeType;
import com.example.bigbrotherbe.domain.campusNotice.util.CampusNoticeMultipartFile;
import com.example.bigbrotherbe.global.file.dto.FileSaveDTO;
import com.example.bigbrotherbe.global.file.entity.File;
import com.example.bigbrotherbe.global.common.enums.EntityType;
import com.example.bigbrotherbe.global.file.service.FileService;
import lombok.*;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CampusNoticeRegisterRequest {
    private String title;
    private String content;
    private CampusNoticeType type;
    private String createAt;
    private String updateAt;
    private List<String> fileUrls;
    private List<String> fileNames;

    public CampusNotice toCampusNoticeEntity(FileService fileService, CampusNoticeType noticeType){
        List<MultipartFile> multipartFiles = this.toFileEntity(fileUrls, fileNames);
        List<File> files = null;
        if (fileService.checkExistRequestFile(multipartFiles)) {
            FileSaveDTO fileSaveDTO = FileSaveDTO.builder()
                    .fileType(EntityType.CAMPUS_NOTICE_TYPE.getType())
                    .multipartFileList(multipartFiles)
                    .build();

            files = fileService.saveFiles(fileSaveDTO);
        }

        CampusNotice campusNotice = CampusNotice.builder()
                .title(title)
                .content(content)
                .type(type)
                .createAt(this.toLocalDateTime(createAt))
                .updateAt(this.toLocalDateTime(updateAt))
                .files(files)
                .type(noticeType)
                .build();

        if (files != null) {
            files.forEach(file -> {
                file.linkCampusNotice(campusNotice);
            });
        }
        return campusNotice;
    }

    private LocalDateTime toLocalDateTime(String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDate date = LocalDate.parse(dateString, formatter);
        return date.atStartOfDay();
    }

    private List<MultipartFile> toFileEntity(List<String> fileUrls, List<String> fileNames){
        List<MultipartFile> multipartFiles = new ArrayList<>();

        for (int i=0 ; i<fileUrls.size() ; i++){
            RestTemplate restTemplate = new RestTemplate();

            byte[] fileContent = restTemplate.getForObject(fileUrls.get(i), byte[].class);
            String fileName = fileNames.get(i);
            String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;

            multipartFiles.add(new CampusNoticeMultipartFile(fileContent, fileName, contentType));
        }
        return multipartFiles;
    }
}
