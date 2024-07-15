package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.Ranking;
import com.example.codewalker.kma.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RankingRepository extends JpaRepository<Ranking,Long> {
    @Query("SELECT r FROM Ranking r WHERE r.student.studentId = :studentId")
    Ranking findByStudentId(@Param("studentId") Long studentId);
    Ranking findByRanking(Long ranking);
    @Query("SELECT r FROM Ranking r WHERE r.student.studentCode = :studentCode")
    Ranking findByStudentCode(@Param("studentCode") String studentCode);
    @Query("SELECT r FROM Ranking r WHERE r.student.studentCode LIKE CONCAT(:mainCode, '%')")
    List<Ranking> findListFilter(@Param("mainCode") String mainCode);
    @Query("SELECT r FROM Ranking r WHERE r.student.studentCode LIKE CONCAT(:mainCode, '%') OR r.student.studentCode LIKE CONCAT(:cyberCode, '%') OR r.student.studentCode LIKE CONCAT(:electronicCode, '%')")
    List<Ranking> findListFilterBlock(@Param("mainCode") String mainCode,
                                      @Param("cyberCode") String cyberCode,
                                      @Param("electronicCode") String electronicCode);
    @Query("SELECT r FROM Ranking r WHERE r.ranking IN (1, 2, 3)")
    List<Ranking> findTopRankingsByStudentCodes(
            @Param("mainCode") String mainCode);

}
