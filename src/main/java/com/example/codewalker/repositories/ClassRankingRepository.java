package com.example.codewalker.repositories;

import com.example.codewalker.models.ClassRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRankingRepository extends JpaRepository<ClassRanking,Long> {
    @Query("SELECT c FROM ClassRanking c WHERE c.student.studentId = :studentId")
    ClassRanking findByStudentId(@Param("studentId") Long studentId);
    ClassRanking findByRanking(Long ranking);
    @Query("SELECT c FROM ClassRanking c WHERE c.student.studentCode = :studentCode")
    ClassRanking findByStudentCode(@Param("studentCode") String studentCode);
    @Query("SELECT c FROM ClassRanking c WHERE c.student.studentCode LIKE :mainCode%")
    List<ClassRanking> findListFilter(@Param("mainCode") String mainCode);
    @Query("SELECT c FROM ClassRanking c WHERE c.student.studentCode LIKE CONCAT(:mainCode, '%') " +
            "OR c.student.studentCode LIKE CONCAT(:cyberCode, '%') OR c.student.studentCode LIKE CONCAT(:electronicCode, '%')")
    List<ClassRanking> findListFilterBlock(@Param("mainCode") String mainCode,
                                           @Param("cyberCode") String cyberCode,
                                           @Param("electronicCode") String electronicCode);
    @Query("SELECT c FROM ClassRanking c WHERE c.ranking IN (1, 2, 3) AND" +
            " c.student.studentCode LIKE %:mainCode%")
    List<ClassRanking> findTopRankingsByStudentCodes(
            @Param("mainCode") String mainCode);
}
