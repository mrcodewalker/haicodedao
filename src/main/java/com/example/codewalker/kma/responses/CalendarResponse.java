package com.example.codewalker.kma.responses;

import com.example.codewalker.kma.dtos.DataDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarResponse {
    @JsonProperty("message")
    private String message;
    @JsonProperty("code")
    private String code;
    @JsonProperty("data")
    private DataDTO dataDTO;
}
