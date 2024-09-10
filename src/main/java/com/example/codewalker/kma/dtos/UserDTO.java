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
public class UserDTO {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password; // Có thể NULL nếu đăng nhập bằng OAuth2

    @JsonProperty("provider_name")
    private String providerName;
    @JsonProperty("github_id")
    private String githubId;
    @JsonProperty("avatar")
    private String avatar;
}
