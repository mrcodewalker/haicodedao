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
public class WannaResponse {
    @JsonProperty("subject_detail")
    private List<SubjectDetailResponse> subjectDetailResponse;
    @JsonProperty("subject_suggest")
    private List<String> subjects;
}
