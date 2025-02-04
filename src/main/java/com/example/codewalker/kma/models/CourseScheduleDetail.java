package com.example.codewalker.kma.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "course_schedule_details")
@Data
public class CourseScheduleDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "course_code")
    private String courseCode;

    @Column(name = "course_id")
    private String courseId;

    @Column(name = "semester")
    private String semester;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "credit_hours")
    private Integer creditHours;

    @Column(name = "ll_code")
    private String llCode;

    @Column(name = "ll_total")
    private String llTotal;

    @Column(name = "class_section")
    private String classSection;

    @Column(name = "study_format")
    private String studyFormat;

    @Column(name = "periods_per_week")
    private Integer periodsPerWeek;

    @Column(name = "day_of_week")
    private Integer dayOfWeek;

    @Column(name = "period_start")
    private String periodStart;

    @Column(name = "period_end")
    private String periodEnd;

    @Column(name = "classroom")
    private String classroom;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "lecturer")
    private String lecturer;

    @Column(name = "bonus_time")
    private Float bonusTime;

    @Column(name = "student_bonus")
    private Float studentBonus;
    @Column(name = "bonus_teacher")
    private Float bonusTeacher;

    @Column(name = "bonus_total")
    private Float bonusTotal;

    @Column(name = "student_quantity")
    private Long studentQuantity;
    @Column(name = "qc")
    private Float qc;
    @Column(name = "description")
    private String description;
    @Column(name = "major")
    private String major;
}
