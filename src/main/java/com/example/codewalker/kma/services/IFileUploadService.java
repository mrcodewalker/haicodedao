package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.FileUploadDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.models.FileUpload;
import com.example.codewalker.kma.responses.FileResponse;
import com.example.codewalker.kma.responses.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFileUploadService {
    FileUploadResponse uploadFile(MultipartFile file, Long userId) throws DataNotFoundException;
    FileResponse historyUpdate(int pageNumber) throws DataNotFoundException;

}
