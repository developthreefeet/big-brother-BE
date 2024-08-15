package com.example.bigbrotherbe.domain.campusNotice.util;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

@RequiredArgsConstructor
public class CampusNoticeMultipartFile implements MultipartFile {

    private final byte[] fileContent;
    private final String fileName;
    private final String contentType;

    @Override
    public String getName() {
        return this.fileName;
    }

    @Override
    public String getOriginalFilename() {
        return this.fileName;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public boolean isEmpty() {
        return fileContent == null || fileContent.length == 0;
    }

    @Override
    public long getSize() {
        return fileContent.length;
    }

    @Override
    public byte[] getBytes() {
        return this.fileContent;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.fileContent);
    }

    @Override
    public void transferTo(File dest) {
        //
    }
}

