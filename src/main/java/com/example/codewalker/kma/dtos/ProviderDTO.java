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
public class ProviderDTO {

    @JsonProperty("provider_id")
    private Long providerId;

    @JsonProperty("provider_name")
    private String providerName;
}
