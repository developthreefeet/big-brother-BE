package com.example.bigbrotherbe.global.file.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.bigbrotherbe.global.exception.BusinessException;
import com.example.bigbrotherbe.global.exception.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import static com.example.bigbrotherbe.global.exception.enums.ErrorCode.FAIL_TO_DELETE;
import static com.example.bigbrotherbe.global.exception.enums.ErrorCode.FAIL_TO_UPLOAD;


@Component
@RequiredArgsConstructor
public class S3Util {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    public String uploadFile(MultipartFile file, String fileType) {
        try {
            String fileName = file.getOriginalFilename();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            String path = fileType + "/" + fileName;

            amazonS3Client.putObject(new PutObjectRequest(bucket, path, file.getInputStream(), metadata));

            // 업로드된 파일의 URL 생성
            URL fileUrl = amazonS3Client.getUrl(bucket, fileName);

            return fileUrl.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(FAIL_TO_UPLOAD);
        } catch (AmazonServiceException e) {
            throw new BusinessException(FAIL_TO_UPLOAD);
        }
    }

    public void deleteFile(String path) {
        try {
            amazonS3Client.deleteObject(bucket, path);
        } catch (AmazonServiceException e) {
            throw new BusinessException(FAIL_TO_DELETE);
        }
    }

}

