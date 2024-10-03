package com.example.bigbrotherbe.file.dto;

import com.example.bigbrotherbe.file.entity.File;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Builder
public class FileUpdateDTO {
    private List<MultipartFile> multipartFileList;
    private List<File> files;
    private String fileType;

    public File toFileEntity(String url) {
        return File.builder()
                .fileType(fileType)
                .url(url)
                .build();
    }
}
