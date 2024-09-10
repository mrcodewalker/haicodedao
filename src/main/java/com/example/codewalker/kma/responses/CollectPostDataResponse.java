package com.example.codewalker.kma.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectPostDataResponse {
    @JsonProperty("post_response")
    private List<PostResponse> postResponse;
    @JsonProperty("page")
    private String page;
    @JsonProperty("size")
    private String size;
    @JsonProperty("total_pages")
    private String totalPages;
}
