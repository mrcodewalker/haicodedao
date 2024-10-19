package com.example.codewalker.kma.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SemesterSubjectsResponse {
    @JsonProperty("subject_name")
    private String subjectName;
    @JsonProperty("subject_credits")
    private Long subjectCredits;
}
