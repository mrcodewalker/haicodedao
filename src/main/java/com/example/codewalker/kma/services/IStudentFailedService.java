package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.CreateScoreDTO;
import com.example.codewalker.kma.dtos.StudentFailedDTO;
import com.example.codewalker.kma.models.Semester;
import com.example.codewalker.kma.models.StudentFailed;

import java.util.List;

public interface IStudentFailedService {
    StudentFailed createStudent(StudentFailedDTO studentFailed);
    List<StudentFailed> findAllRecords();
    List<StudentFailed> findByStudentCode(String studentCode);
}
