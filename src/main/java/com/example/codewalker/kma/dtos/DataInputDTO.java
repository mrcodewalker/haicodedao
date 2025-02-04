package com.example.codewalker.kma.dtos;

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
public class DataInputDTO {
    private String courseName;
    private Long studentQuantity;
    private String description;
}
