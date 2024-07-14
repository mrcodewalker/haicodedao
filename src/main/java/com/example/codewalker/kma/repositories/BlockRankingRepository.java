package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.BlockRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockRankingRepository extends JpaRepository<BlockRanking,Long> {
    @Query("SELECT b FROM BlockRanking b WHERE b.student.studentId = :studentId")
    BlockRanking findByStudentId(@Param("studentId") Long studentId);
    BlockRanking findByRanking(Long ranking);
    @Query("SELECT b FROM BlockRanking b WHERE b.student.studentCode = :studentCode")
    BlockRanking findByStudentCode(@Param("studentCode") String studentCode);

    @Query("SELECT b FROM BlockRanking b WHERE b.student.studentCode LIKE :mainCode%")
    List<BlockRanking> findListFilter(@Param("mainCode") String mainCode);
    @Query("SELECT b FROM BlockRanking b WHERE b.student.studentCode LIKE CONCAT(:mainCode, '%') OR" +
            " b.student.studentCode LIKE CONCAT(:cyberCode, '%') OR b.student.studentCode LIKE CONCAT(:electronicCode, '%')")
    List<BlockRanking> findListFilterBlock(@Param("mainCode") String mainCode,
                                           @Param("cyberCode") String cyberCode,
                                           @Param("electronicCode") String electronicCode);
    @Query("SELECT br FROM BlockRanking br WHERE br.ranking IN (2, 1, 3) AND (br.student.studentCode LIKE %:mainCode% OR br.student.studentCode LIKE %:cyberCode% OR br.student.studentCode LIKE %:electronicCode%)")
    List<BlockRanking> findTopRankingsByStudentCodes(
            @Param("mainCode") String mainCode,
            @Param("cyberCode") String cyberCode,
            @Param("electronicCode") String electronicCode);
}