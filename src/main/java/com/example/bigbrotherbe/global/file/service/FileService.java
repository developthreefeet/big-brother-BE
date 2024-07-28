package com.example.bigbrotherbe.global.file.service;

import com.example.bigbrotherbe.global.file.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileService {

    private final S3Util s3Util;


}
