package com.example.codewalker.kma.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Data
@Builder
@Component
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadDTO {
    @JsonProperty("file")
    private MultipartFile file;
    @JsonProperty("id")
    private Long userId;
}
