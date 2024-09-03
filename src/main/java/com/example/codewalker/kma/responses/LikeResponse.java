package com.example.codewalker.kma.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeResponse {
    @JsonProperty("like_id")
    private Long likeId;

    @JsonProperty("post_id")
    private Long postId;

    @JsonProperty("author_name")
    private String authorName;
}
