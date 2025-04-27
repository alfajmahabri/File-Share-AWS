package com.example.fileShare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


@Entity
@Data
public class FileData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long pin;
    private String filename;
    private String extension;
    private int count;
}
