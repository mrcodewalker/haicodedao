package com.example.codewalker.repositories;

import com.example.codewalker.models.Ranking;
import com.example.codewalker.models.SemesterRanking;
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
    @Query("SELECT s FROM SemesterRanking s WHERE s.student.studentCode LIKE :mainCode%")
    List<Ranking> findListFilter(@Param("mainCode") String mainCode);
}
