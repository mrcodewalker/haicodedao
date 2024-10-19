package com.example.codewalker.kma.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectUsersResponse {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("username")
    private String username;
    @JsonProperty("email")
    private String email;
    @JsonProperty("provider_name")
    private String providerName;
    @JsonProperty("role_name")
    private String roleName;
    @JsonProperty("github_id")
    private String githubId;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("is_active")
    private boolean isActive;
}
