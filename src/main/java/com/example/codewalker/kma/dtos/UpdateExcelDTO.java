package com.example.codewalker.kma.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
@Builder
public class UpdateExcelDTO {
    @JsonProperty("code")
    private List<String> courseCode;
    @JsonProperty("major")
    private List<String> major;
}
