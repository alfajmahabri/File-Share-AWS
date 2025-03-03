package com.example.fileShare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.Random;

@Service
public class S3UploadService {
    @Autowired
    private S3Presigner presigner;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public String generatePresignedUrl(){
        String key = generateRandom();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key+".pdf")
                .build();
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .putObjectRequest(putObjectRequest)
                .signatureDuration(Duration.ofMinutes(15))
                .build();

        try{
            return presigner.presignPutObject(presignRequest).url().toString();
        }catch (Exception e){
            throw new RuntimeException("Error generating presigned URL", e);
        }
    }

    private String generateRandom() {
        Random random = new Random();
        return String.valueOf(1000 + random.nextInt(9000));
    }

}