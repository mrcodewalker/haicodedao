package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.FileUploadDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.models.FileUpload;
import com.example.codewalker.kma.models.User;
import com.example.codewalker.kma.repositories.FileUploadRepository;
import com.example.codewalker.kma.repositories.UserRepository;
import com.example.codewalker.kma.responses.FileResponse;
import com.example.codewalker.kma.responses.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileUploadService implements IFileUploadService{
    private final FileUploadRepository fileUploadRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    @Override
    @Transactional
    @CreationTimestamp
    public FileUploadResponse uploadFile(MultipartFile file, Long userId) throws DataNotFoundException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File không được rỗng");
        }

        String encodedFileName = Base64.getEncoder().encodeToString(file.getOriginalFilename().getBytes(StandardCharsets.UTF_8));

        if (fileUploadRepository.existsByFileName(encodedFileName)) {

            FileUpload fileUpload = this.fileUploadRepository.findByFileName(encodedFileName);

            fileUpload.setCreatedAt(LocalDateTime.now());
            fileUpload.setUpdatedAt(LocalDateTime.now());

            this.fileUploadRepository.save(fileUpload);

            return FileUploadResponse.builder()
                    .fileName("MR.CODEWALKER")
                    .build();
        }

        FileUpload fileUpload = FileUpload.builder()
                .fileName(encodedFileName)  // Lưu tên file mã hóa
                .fileDescription("")
                .fileSize(file.getSize())   // Kích thước file
                .fileType(file.getContentType()) // Loại file
                .user(userService.getUserById(userId)) // Ánh xạ userId
                .build();

        // Lưu vào cơ sở dữ liệu
        FileUpload savedFile = this.fileUploadRepository.save(fileUpload);

        return FileUploadResponse.builder()
                .fileDescription(savedFile.getFileDescription())
                .fileName(decodeBase64(savedFile.getFileName()))
                .fileSize(savedFile.getFileSize())
                .fileType(savedFile.getFileType())
                .createdAt(savedFile.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
                .build();
    }

    @Override
    public FileResponse historyUpdate(int pageNumber) throws DataNotFoundException {
        // Mỗi trang có tối đa 10 file
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<FileUpload> page = this.fileUploadRepository.findAllByOrderByCreatedAtDesc(pageable);
        List<FileUploadResponse> responses = new ArrayList<>();

        for (FileUpload clone : page.getContent()) {
            responses.add(
                    FileUploadResponse.builder()
                            .fileDescription(clone.getFileDescription())
                            .fileName(decodeBase64(clone.getFileName()))
                            .fileSize(clone.getFileSize())
                            .fileType(clone.getFileType())
                            .createdAt(clone.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
                            .build()
            );
        }

        return FileResponse.builder()
                .fileUploadResponseList(responses)
                .totalPages((long) page.getTotalPages())
                .build();
    }


    public static String decodeBase64(String base64String) {
        if (base64String == null || base64String.isEmpty()) {
            return null;
        }

        byte[] decodedBytes = Base64.getDecoder().decode(base64String);
        return new String(decodedBytes);
    }
}
