package com.example.codewalker.kma.services;

import com.example.codewalker.kma.models.Schedule;
import com.example.codewalker.kma.responses.ScheduleResponse;
import com.example.codewalker.kma.responses.SubjectDetailResponse;
import com.example.codewalker.kma.responses.SubjectResponse;
import com.example.codewalker.kma.responses.WannaResponse;

import java.util.List;

public interface IScheduleService {
    Schedule createSchedule(Schedule schedule);
    List<ScheduleResponse> findByStudentCourse(String studentCourse);
    List<SubjectResponse> findAllSubjects(String studentCourse);
    SubjectDetailResponse findScheduleTime(String subjectName);
    WannaResponse findRequestSchedule(String studentCourse, List<String> dayInWeek);

}
