package com.example.codewalker.kma.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
@Builder
public class CommentDTO {

    @JsonProperty("comment_id")
    private Long commentId;

    @JsonProperty("post_id")
    private Long postId;

    @JsonProperty("content")
    private String content;

    @JsonProperty("author_id")
    private String authorId;
}
