package com.example.codewalker.kma.dtos;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
@Builder
public class ExportFileDTO {
    private String path;
    private String semester;
}
