package com.example.codewalker.kma.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
@Builder
public class UpdateDataDTO {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("content")
    private String content;
}
