package com.example.codewalker.repositories;

import com.example.codewalker.models.BlockDetailRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockDetailRankingRepository extends JpaRepository<BlockDetailRanking,Long> {
    @Query("SELECT b FROM BlockDetailRanking b WHERE b.student.studentId = :studentId")
    BlockDetailRanking findByStudentId(@Param("studentId") Long studentId);
    BlockDetailRanking findByRanking(Long ranking);
    @Query("SELECT b FROM BlockDetailRanking b WHERE b.student.studentCode = :studentCode")
    BlockDetailRanking findByStudentCode(@Param("studentCode") String studentCode);

    @Query("SELECT b FROM BlockDetailRanking b WHERE b.student.studentCode LIKE :mainCode%")
    List<BlockDetailRanking> findListFilter(@Param("mainCode") String mainCode);
    @Query("SELECT b FROM BlockDetailRanking b WHERE b.student.studentCode LIKE CONCAT(:mainCode, '%') " +
            "OR b.student.studentCode LIKE CONCAT(:cyberCode, '%') OR b.student.studentCode LIKE CONCAT(:electronicCode, '%')")
    List<BlockDetailRanking> findListFilterBlock(@Param("mainCode") String mainCode,
                                           @Param("cyberCode") String cyberCode,
                                           @Param("electronicCode") String electronicCode);
    @Query("SELECT b FROM BlockDetailRanking b WHERE b.ranking IN (1, 2, 3) AND" +
            " b.student.studentCode LIKE %:mainCode%")
    List<BlockDetailRanking> findTopRankingsByStudentCodes(
            @Param("mainCode") String mainCode);
}
