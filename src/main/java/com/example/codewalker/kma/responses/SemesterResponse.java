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
public class SemesterResponse {
    @JsonProperty("subjects_list")
    List<SubjectResponse> subjectResponses;
    @JsonProperty("ranking_list")
    List<SemesterRankingResponse> rankingResponses;
}
