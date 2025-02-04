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
public class ListDataDTO {
    @JsonProperty("data")
    List<DataInputDTO> list;
}
