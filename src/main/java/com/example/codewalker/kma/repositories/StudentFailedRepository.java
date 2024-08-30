package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.StudentFailed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentFailedRepository extends JpaRepository<StudentFailed, Long> {
    List<StudentFailed> findAll();
    List<StudentFailed> findByStudentCode(String studentCode);
}
