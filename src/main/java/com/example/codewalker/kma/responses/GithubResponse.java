package com.example.codewalker.kma.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GithubResponse {
    @JsonProperty("username")
    private String username;

    @JsonProperty("fullname")
    private String fullname;

    @JsonProperty("github_id")
    private String githubId;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    @JsonProperty("email")
    private String email;
}
