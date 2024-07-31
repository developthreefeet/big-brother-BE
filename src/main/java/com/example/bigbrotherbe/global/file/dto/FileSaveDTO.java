package com.example.bigbrotherbe.global.file.dto;

import com.example.bigbrotherbe.global.file.entity.File;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
public class FileSaveDTO {
    private List<MultipartFile> multipartFileList;
    private String fileType;

    public File toFileEntity(String url) {
        return File.builder()
                .fileType(fileType)
                .url(url)
                .build();
    }
}
