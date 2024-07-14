package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.BlockRanking;
import com.example.codewalker.kma.models.ClassRanking;
import com.example.codewalker.kma.models.Ranking;
import com.example.codewalker.kma.models.Scholarship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
    @Query("SELECT s FROM Scholarship s WHERE s.ranking IN (1, 2, 3) AND" +
            " s.student.studentCode LIKE %:mainCode%")
    List<Scholarship> findTopRankingsByStudentCodes(
            @Param("mainCode") String mainCode);
}
