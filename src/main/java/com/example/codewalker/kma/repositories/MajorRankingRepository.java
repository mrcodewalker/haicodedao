package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.MajorRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MajorRankingRepository extends JpaRepository<MajorRanking,Long> {
    @Query("SELECT m FROM MajorRanking m WHERE m.student.studentId = :studentId")
    MajorRanking findByStudentId(@Param("studentId") Long studentId);
    MajorRanking findByRanking(Long ranking);
    @Query("SELECT m FROM MajorRanking m WHERE m.student.studentCode = :studentCode")
    MajorRanking findByStudentCode(@Param("studentCode") String studentCode);

    @Query("SELECT m FROM MajorRanking m WHERE m.student.studentCode LIKE :mainCode%")
    List<MajorRanking> findListFilter(@Param("mainCode") String mainCode);
    @Query("SELECT m FROM MajorRanking m WHERE m.student.studentCode LIKE CONCAT(:mainCode, '%') " +
            "OR m.student.studentCode LIKE CONCAT(:cyberCode, '%') OR m.student.studentCode LIKE CONCAT(:electronicCode, '%')")
    List<MajorRanking> findListFilterBlock(@Param("mainCode") String mainCode,
                                      @Param("cyberCode") String cyberCode,
                                      @Param("electronicCode") String electronicCode);
    @Query("SELECT m FROM MajorRanking m WHERE m.ranking IN (1, 2, 3) AND" +
            " m.student.studentCode LIKE %:mainCode%")
    List<MajorRanking> findTopRankingsByStudentCodes(
            @Param("mainCode") String mainCode);
    @Modifying
    @Transactional
    @Query("DELETE FROM MajorRanking")
    void deleteAllRecords();
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE major_ranking AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}