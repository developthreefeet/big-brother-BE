package com.example.bigbrotherbe.global.file.service;

import com.example.bigbrotherbe.global.file.dto.FileSaveDTO;
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
    @Transactional
    public List<File> saveFile(FileSaveDTO fileSaveDTO) {
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
}
