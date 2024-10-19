package com.example.codewalker.kma.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileResponse {
    @JsonProperty("files")
    private List<FileUploadResponse> fileUploadResponseList;
    @JsonProperty("totalPages")
    private Long totalPages;
}
