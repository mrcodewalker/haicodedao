package com.example.codewalker.kma.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileUploadResponse {
    @JsonProperty("file_name")
    private String fileName;
    @JsonProperty("file_description")
    private String fileDescription;
    @JsonProperty("file_size")
    private Long fileSize;
    @JsonProperty("file_type")
    private String fileType;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("total")
    private Long totalPages;
}
