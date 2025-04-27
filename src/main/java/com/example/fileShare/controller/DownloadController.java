package com.example.fileShare.controller;

import com.example.fileShare.model.DownloadResponse;
import com.example.fileShare.model.UploadResponse;
import com.example.fileShare.service.S3DownloadService;
import com.example.fileShare.service.S3UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DownloadController {

    @Autowired
    private S3DownloadService downloadService;
    @Autowired
    private S3UploadService uploadService;

    @GetMapping("/download")
    public ResponseEntity<DownloadResponse> downloadFile(@RequestParam String key) {
        if (downloadService.checkFile(key)) {
            DownloadResponse downloadResponse = downloadService.generatePresignedUrl(key);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(downloadResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        UploadResponse uploadResponse = uploadService.generatePresignedUrl(file);
        return ResponseEntity.ok(uploadResponse);
    }

}