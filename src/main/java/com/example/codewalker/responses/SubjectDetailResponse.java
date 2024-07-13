package com.example.codewalker.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectDetailResponse {
    @JsonProperty("subject_credits")
    private Long subjectCredits;
    @JsonProperty("subject_name")
    private String subjectName;
    @JsonProperty("day_in_week")
    private List<Long> dayInWeek;
    @JsonProperty("lesson_number")
    private List<String> lessonNumber;
    @JsonProperty("start_day")
    private List<Date> startDay;
    @JsonProperty("end_day")
    private List<Date> endDay;
    @JsonProperty("schedule")
    private DateResponse schedule;
    @JsonProperty("schedule_end")
    private List<Date> scheduleEnd;
}
