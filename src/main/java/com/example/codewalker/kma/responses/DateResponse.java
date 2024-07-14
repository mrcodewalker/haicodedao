package com.example.codewalker.kma.responses;

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
public class DateResponse {
    @JsonProperty("day_in_week")
    private List<Long> dayInWeek;
    @JsonProperty("schedule")
    private List<Date> schedule;
}
