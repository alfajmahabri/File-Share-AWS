package com.example.fileShare.repository;

import com.example.fileShare.model.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileDataRepository extends JpaRepository<FileData,Long> {
    FileData findByPin(long key);
}
