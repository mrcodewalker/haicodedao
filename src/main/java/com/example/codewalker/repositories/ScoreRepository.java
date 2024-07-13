package com.example.codewalker.repositories;

import com.example.codewalker.models.Score;
import com.example.codewalker.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ScoreRepository extends JpaRepository<Score,Long> {
    List<Score> findByStudent(Student student);
    @Query("SELECT s FROM Score s WHERE s.student.studentCode = :studentCode")
    List<Score> findByStudentCode(@Param("studentCode") String studentCode);
    @Query("SELECT s FROM Score s WHERE s.student.studentId = :studentId")
    List<Score> findByStudentId(@Param("studentId") Long studentId);
}
