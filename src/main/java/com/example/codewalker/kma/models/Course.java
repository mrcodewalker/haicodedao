package com.example.codewalker.kma.models;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;
//@Entity
//@Table(name = "courses")
//@Data
//public class Course {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String courseCode;
//    private Integer creditHours;
//    private String llCode;
//    private String classSection;
//    private String studyFormat;
//    private Integer periodsPerWeek;
//    private LocalDate startDate;
//    private LocalDate endDate;
//
//    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
//    private List<CourseSchedule> schedules;
//}