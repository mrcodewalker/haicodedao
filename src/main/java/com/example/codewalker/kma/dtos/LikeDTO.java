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
public class LikeDTO {

    @JsonProperty("post_id")
    private Long postId;

    @JsonProperty("author_id")
    private String authorId;

}
