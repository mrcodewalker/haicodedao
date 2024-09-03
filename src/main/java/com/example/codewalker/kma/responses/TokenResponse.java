package com.example.codewalker.kma.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse {
    @JsonProperty("token")
    private String token;
    @JsonProperty("status")
    private String status;
}
