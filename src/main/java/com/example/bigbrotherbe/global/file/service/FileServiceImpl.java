package com.example.bigbrotherbe.global.file.service;

import com.example.bigbrotherbe.global.file.dto.FileDeleteDTO;
import com.example.bigbrotherbe.global.file.dto.FileSaveDTO;
import com.example.bigbrotherbe.global.file.dto.FileUpdateDTO;
import com.example.bigbrotherbe.global.file.entity.File;
import com.example.bigbrotherbe.global.file.repository.FileRepository;
import com.example.bigbrotherbe.global.file.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final S3Util s3Util;
    private final FileRepository fileRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<File> saveFiles(FileSaveDTO fileSaveDTO) {
        List<File> files = new ArrayList<>();

        String fileType = fileSaveDTO.getFileType();
        List<MultipartFile> multipartFileList = fileSaveDTO.getMultipartFileList();

        multipartFileList.forEach(file -> {
            String url = s3Util.uploadFile(file, fileType);
            File fileEntity = fileRepository.save(fileSaveDTO.toFileEntity(url));
            files.add(fileEntity);
        });

        return files;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public File saveFile(FileSaveDTO fileSaveDTO) {
        String fileType = fileSaveDTO.getFileType();
        MultipartFile file = fileSaveDTO.getMultipartFile();
        String url = s3Util.uploadFile(file, fileType);
        return fileRepository.save(fileSaveDTO.toFileEntity(url));
    }

    @Override
    @Transactional
    public List<File> updateFile(FileUpdateDTO fileUpdateDTO) {
        List<File> updatedFiles = new ArrayList<>();

        List<File> files = fileUpdateDTO.getFiles();
        List<MultipartFile> multipartFileList = fileUpdateDTO.getMultipartFileList();
        String fileType = fileUpdateDTO.getFileType();

        files.forEach(file -> {
            String fileName = file.getUrl().split("/")[3];
            s3Util.deleteFile(fileType + "/" + fileName);
        });

        multipartFileList.forEach(file -> {
            String url = s3Util.uploadFile(file, fileType);
            files.forEach(fileEntity -> {
                fileEntity.update(url);
                updatedFiles.add(fileEntity);
            });
        });
        return updatedFiles;
    }

    public void deleteFile(FileDeleteDTO deleteDTO) {
        List<File> files = deleteDTO.getFiles();

        if (files == null || files.isEmpty()) {
            return;
        }

        String fileType = deleteDTO.getFileType();
        files.forEach(file -> {
            String fileName = file.getUrl().split("/")[3];
            s3Util.deleteFile(fileType + "/" + fileName);
        });
    }

    @Override
    public boolean checkExistRequestFile(List<MultipartFile> multipartFiles) {
        if (multipartFiles == null) {
            return false;
        }

        for (MultipartFile file : multipartFiles) {
            if (file.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
