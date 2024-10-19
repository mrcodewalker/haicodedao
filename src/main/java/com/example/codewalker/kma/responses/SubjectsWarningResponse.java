package com.example.codewalker.kma.responses;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectsWarningResponse {
    @JsonProperty("subject_name")
    private String subjectName;
    @JsonProperty("graph")
    private List<GraphFilterSubjectResponse> graphFilterSubjectResponseList;
}
