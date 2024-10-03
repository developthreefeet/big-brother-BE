package com.example.bigbrotherbe.file.dto;

import com.example.bigbrotherbe.file.entity.File;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
public class FileSaveDTO {
    private List<MultipartFile> multipartFileList;
    private MultipartFile multipartFile;
    private String fileType;

    public File toFileEntity(String url, String fileName) {
        return File.builder()
                .fileType(fileType)
                .url(url)
                .fileName(fileName)
                .build();
    }
}
