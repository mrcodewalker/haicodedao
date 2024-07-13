package com.example.codewalker.services;

import com.example.codewalker.models.Student;
import com.example.codewalker.responses.StudentResponse;

import java.util.List;

public interface IStudentService {
    Student findByStudentCode(String studentCode);
    Student findById(Long id);
    List<Student> findByName(String studentName);
    List<Student> findByClass(String studentClass);
    Student createStudent(Student student);
    boolean existByStudentCode(String studentCode);
    List<StudentResponse> getStudentsByName(String studentName);
}
