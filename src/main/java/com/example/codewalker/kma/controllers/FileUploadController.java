package com.example.codewalker.kma.controllers;

import com.example.codewalker.kma.dtos.FileUploadDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.services.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/file")
@CrossOrigin(origins = "https://kma-legend.onrender.com")
public class FileUploadController{
    private final FileUploadService fileUploadService;
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("id") Long userId) throws DataNotFoundException {
        try {
            return ResponseEntity.ok(fileUploadService.uploadFile(file, userId));
        } catch (Exception e){
            return ResponseEntity.ok("Can not upload file right now!");
        }
    }
    @GetMapping("/download")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<?> getHistory(
            @RequestParam(defaultValue = "0") int pageNumber
    ) throws DataNotFoundException {
        try {
            return ResponseEntity.ok(fileUploadService.historyUpdate(pageNumber));
        } catch (Exception e){
            return ResponseEntity.ok("Can not upload file right now!");
        }
    }
}
