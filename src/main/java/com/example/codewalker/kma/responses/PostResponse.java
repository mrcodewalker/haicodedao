package com.example.codewalker.kma.responses;

import com.example.codewalker.kma.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
    @JsonProperty("post_id")
    private Long postId;

    @JsonProperty("content")
    private String content;

    @JsonProperty("author_name")
    private String authorName;

    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
