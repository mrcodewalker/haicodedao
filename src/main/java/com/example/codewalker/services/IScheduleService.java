package com.example.codewalker.services;

import com.example.codewalker.models.Schedule;
import com.example.codewalker.responses.ScheduleResponse;
import com.example.codewalker.responses.SubjectDetailResponse;
import com.example.codewalker.responses.SubjectResponse;
import com.example.codewalker.responses.WannaResponse;

import java.util.List;

public interface IScheduleService {
    Schedule createSchedule(Schedule schedule);
    List<ScheduleResponse> findByStudentCourse(String studentCourse);
    List<SubjectResponse> findAllSubjects(String studentCourse);
    SubjectDetailResponse findScheduleTime(String subjectName);
    WannaResponse findRequestSchedule(String studentCourse, List<String> dayInWeek);

}
