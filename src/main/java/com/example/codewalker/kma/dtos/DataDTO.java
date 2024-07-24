package com.example.codewalker.kma.dtos;

import com.example.codewalker.kma.responses.StudentDataResponse;
import com.example.codewalker.kma.responses.TimelineResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
@Builder
public class DataDTO {
    @JsonProperty("student_info")
    private StudentDataResponse studentDataResponse;
    @JsonProperty("student_schedule")
    private List<TimelineResponse> timelineResponse;
}
