package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.Ranking;
import com.example.codewalker.kma.models.SemesterRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SemesterRankingRepository extends JpaRepository<SemesterRanking,Long> {
    @Query("SELECT s FROM SemesterRanking s WHERE s.student.studentId = :studentId")
    SemesterRanking findByStudentId(@Param("studentId") Long studentId);
    SemesterRanking findByRanking(Long ranking);
    @Query("SELECT s FROM SemesterRanking s WHERE s.student.studentCode = :studentCode")
    SemesterRanking findByStudentCode(@Param("studentCode") String studentCode);
    @Query("SELECT s FROM SemesterRanking s WHERE s.student.studentCode LIKE :mainCode%")
    List<SemesterRanking> findListFilter(@Param("mainCode") String mainCode);
    @Query("SELECT s FROM SemesterRanking s WHERE s.ranking BETWEEN 1 AND 100 ORDER BY s.ranking ASC")
    List<SemesterRanking> findTop100();

}
