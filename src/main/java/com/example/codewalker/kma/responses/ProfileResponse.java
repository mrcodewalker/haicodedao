package com.example.codewalker.kma.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponse {
    @JsonProperty("username")
    private String username;
    @JsonProperty("email")
    private String email;

    @JsonProperty("provider_name")
    private String providerName;

    @JsonProperty("avatar")
    private String avatar;
}
