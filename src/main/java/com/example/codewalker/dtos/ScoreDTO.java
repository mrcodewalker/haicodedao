package com.example.codewalker.dtos;

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
public class ScoreDTO {
    @JsonProperty("student_id")
    private Long studentId;

    @JsonProperty("subject_id")
    private Long subjectId;

    @JsonProperty("score_text")
    private String scoreText;

    @JsonProperty("score_first")
    private Float scoreFirst;

    @JsonProperty("score_second")
    private Float scoreSecond;

    @JsonProperty("score_final")
    private Float scoreFinal;

    @JsonProperty("score_over_rall")
    private Float scoreOverall;
}
