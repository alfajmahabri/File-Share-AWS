package com.example.fileShare.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DownloadResponse {
    private String filename;
    private String extension;
    private String url;
}
