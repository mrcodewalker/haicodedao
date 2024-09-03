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
public class PostDTO {

    @JsonProperty("content")
    private String content;

    @JsonProperty("author_id")
    private String authorId;

    @JsonProperty("imageUrl")
    private String imageUrl;
}
