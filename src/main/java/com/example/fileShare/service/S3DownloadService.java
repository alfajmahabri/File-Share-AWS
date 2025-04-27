package com.example.fileShare.service;

import com.example.fileShare.model.DownloadResponse;
import com.example.fileShare.model.FileData;
import com.example.fileShare.repository.FileDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import java.net.MalformedURLException;
import java.time.Duration;

@Service
public class S3DownloadService {
    @Autowired
    private S3Presigner presigner;
    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Autowired
    private FileDataRepository fileDataRepository;

    public DownloadResponse generatePresignedUrl(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(Duration.ofMinutes(10))
                .build();

        try {
            String presignedUrl = presigner.presignGetObject(presignRequest).url().toString();
            FileData fileData = fileDataRepository.findByPin(Long.parseLong(key));
            String filename = fileData.getFilename();
            String extension = fileData.getExtension();
            return new DownloadResponse(filename,extension,presignedUrl);
        } catch (Exception e) {
            throw new RuntimeException("Error generating presigned URL", e);
        }
    }

    public boolean checkFile(String key){
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            s3Client.headObject(headObjectRequest);
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }
}