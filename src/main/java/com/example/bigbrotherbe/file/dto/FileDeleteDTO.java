package com.example.bigbrotherbe.file.dto;

import com.example.bigbrotherbe.file.entity.File;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FileDeleteDTO {
    private List<File> files;
    private String fileType;
}
