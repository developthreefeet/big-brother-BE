package com.example.bigbrotherbe.global.file.service;

import com.example.bigbrotherbe.global.file.dto.FileSaveDTO;
import com.example.bigbrotherbe.global.file.entity.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<File> saveFile(FileSaveDTO fileSaveDTO);
//    void updateFile();
//    void deleteFile();
}
