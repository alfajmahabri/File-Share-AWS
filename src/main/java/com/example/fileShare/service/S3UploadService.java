package com.example.fileShare.service;

import com.example.fileShare.model.FileData;
import com.example.fileShare.model.UploadResponse;
import com.example.fileShare.repository.FileDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import com.example.fileShare.model.UploadResponse;

import java.time.Duration;
import java.util.Random;

@Service
public class S3UploadService {
    @Autowired
    private S3Presigner presigner;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Autowired
    private FileDataRepository fileDataRepository;

    public UploadResponse generatePresignedUrl(MultipartFile file){
        String key = generateRandom();
        FileData fileData = new FileData();
        fileData.setPin(Long.parseLong(key));
        fileData.setFilename((getFileName(file.getOriginalFilename())));
        fileData.setExtension((getFileExtension(file.getOriginalFilename())));
        fileData.setCount(3);
        fileDataRepository.save(fileData);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .putObjectRequest(putObjectRequest)
                .signatureDuration(Duration.ofMinutes(15))
                .build();

        try{
            String url = presigner.presignPutObject(presignRequest).url().toString();
            return new UploadResponse(url, key);
        }catch (Exception e){
            throw new RuntimeException("Error generating presigned URL", e);
        }
    }

    private String getFileExtension(String originalFilename) {
        if(originalFilename==null){
            return null;
        }
        int dontIndex =originalFilename.lastIndexOf('.');
        return originalFilename.substring(dontIndex+1);
    }

    private String getFileName(String originalFilename) {
        if(originalFilename==null){
            return null;
        }
        int dontIndex =originalFilename.lastIndexOf('.');
        return originalFilename.substring(0,dontIndex);
    }

    private String generateRandom() {
        Random random = new Random();
        return String.valueOf(1000 + random.nextInt(9000));
    }

}