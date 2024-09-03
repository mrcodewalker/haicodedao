package com.example.codewalker.kma.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    @JsonProperty("comment_id")
    private String commentId;
    @JsonProperty("post_id")
    private String postId;
    @JsonProperty("content")
    private String content;
    @JsonProperty("author_name")
    private String authorName;
}
