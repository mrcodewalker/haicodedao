package com.example.codewalker.kma.dtos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Data
@Builder
@Component
@AllArgsConstructor
@NoArgsConstructor
public class GraphRequestDTO {
    @JsonProperty("subject_name")
    private String subjectName;
    @JsonProperty("year_course")
    private String yearCourse;
}
