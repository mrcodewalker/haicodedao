package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.FileUpload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {
    Page<FileUpload> findAllByOrderByCreatedAtDesc(Pageable pageable);
    boolean existsByFileName(String fileName);
    FileUpload findByFileName(String fileName);
}
