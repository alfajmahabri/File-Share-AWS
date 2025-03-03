package com.example.fileShare.controller;

import com.example.fileShare.service.S3DownloadService;
import com.example.fileShare.service.S3UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
public class DownloadController {

    @Autowired
    private S3DownloadService downloadService;
    @Autowired
    private S3UploadService uploadService;

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String key) {
        if(downloadService.checkFile(key)){
            Resource resource = downloadService.generatePresignedUrl(key);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + key + ".pdf\"")
                    //we used header so that browser will trigger download dialog
                    //if we dont then it will just display
                    .body(resource);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(Model model) {
        String presignedUrl = String.valueOf(uploadService.generatePresignedUrl());
        return ResponseEntity.ok(presignedUrl);
    }

}