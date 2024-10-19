package com.example.codewalker.kma.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GraphFilterSubjectResponse {
    @JsonProperty("subject_name")
    private String subjectName;
    @JsonProperty("score")
    private List<String> score;
    @JsonProperty("count")
    private List<String> count;
    @JsonProperty("total")
    private Long total;
    @JsonProperty("equals_zero")
    private Long equalsZero;
    @JsonProperty("less_than_six")
    private Long lessThanSix;
    @JsonProperty("less_than_eight")
    private Long lessThanEight;
    @JsonProperty("less_than_nine")
    private Long lessThanNine;
    @JsonProperty("greater_than_nine")
    private Long greaterThanNine;
    @JsonProperty("less_than_four")
    private Long lessThanFour;
    @JsonProperty("eight_to_ten")
    private Long eightToTen;
    @JsonProperty("seven_to_eight")
    private Long sevenToEight;
    @JsonProperty("five_to_seven")
    private Long fiveToSeven;
    @JsonProperty("less_than_five")
    private Long lessThanFive;
    @JsonProperty("valid")
    private boolean valid;
    @JsonProperty("alert_message")
    private List<String> alertMessage;
}
