package com.example.codewalker.kma.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDataResponse {
    @JsonProperty("birthday")
    private String birthday;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("student_code")
    private String studentCode;
    @JsonProperty("display_name")
    private String displayName;
}
