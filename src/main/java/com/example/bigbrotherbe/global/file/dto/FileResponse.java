package com.example.bigbrotherbe.global.file.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FileResponse {
    private String fileName;
    private String url;

    public static FileResponse of(String fileName, String url) {
        return FileResponse.builder()
                .fileName(fileName)
                .url(url)
                .build();
    }
}
