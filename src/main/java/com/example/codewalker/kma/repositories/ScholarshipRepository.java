package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository

public interface ScholarshipRepository extends JpaRepository<Scholarship,Long> {
    @Query("SELECT s FROM Scholarship s WHERE s.student.studentId = :studentId")
    Scholarship findByStudentId(@Param("studentId") Long studentId);
    Scholarship findByRanking(Long ranking);
    @Query("SELECT s FROM Scholarship s WHERE s.student.studentCode = :studentCode")
    Scholarship findByStudentCode(@Param("studentCode") String studentCode);

    @Query("SELECT s FROM Scholarship s WHERE s.student.studentCode LIKE :mainCode%")
    List<Scholarship> findListFilter(@Param("mainCode") String mainCode);
    @Query("SELECT s FROM Scholarship s WHERE s.ranking IN (2, 1, 3) AND" +
            " s.student.studentCode LIKE %:mainCode%")
    List<Scholarship> findTopRankingsByStudentCodes(
            @Param("mainCode") String mainCode);
    @Query("SELECT s FROM Scholarship s WHERE s.ranking BETWEEN 1 AND 100 ORDER BY s.ranking ASC, s.gpa DESC")
    List<Scholarship> findTop100(Pageable pageable);
    @Modifying
    @Transactional
    @Query("DELETE FROM Scholarship")
    void deleteAllRecords();
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE scholarship AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
