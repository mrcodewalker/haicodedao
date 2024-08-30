package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.StudentFailedDTO;
import com.example.codewalker.kma.models.StudentFailed;
import com.example.codewalker.kma.repositories.StudentFailedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentFailedService implements IStudentFailedService{
    private final StudentFailedRepository studentFailedRepository;
    @Override
    public StudentFailed createStudent(StudentFailedDTO studentFailed) {
        return this.studentFailedRepository.save(
                StudentFailed.builder()
                        .studentCode(studentFailed.getStudentCode())
                .build());
    }

    @Override
    public List<StudentFailed> findAllRecords() {
        return this.studentFailedRepository.findAll();
    }

    @Override
    public List<StudentFailed> findByStudentCode(String studentCode) {
        return this.studentFailedRepository.findByStudentCode(studentCode);
    }
}
