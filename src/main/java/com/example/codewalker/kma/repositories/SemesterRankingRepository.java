package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.Ranking;
import com.example.codewalker.kma.models.SemesterRanking;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@EnableJpaRepositories
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

//        @Query("SELECT sr FROM SemesterRanking sr JOIN sr.student s WHERE sr.ranking BETWEEN 1 AND 40 AND s.studentCode LIKE CONCAT(:filterCode, '%') ORDER BY sr.ranking ASC")
//        List<SemesterRanking> findTopWithMatchingFilterCode(@Param("filterCode") String filterCode, Pageable pageable);
    @Modifying
    @Transactional
    @Query("DELETE FROM SemesterRanking")
    void deleteAllRecords();
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE semester_ranking AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
    @Query("SELECT sr FROM SemesterRanking sr JOIN sr.student st WHERE st.studentCode LIKE :studentCodePrefix OR " +
            "st.studentCode LIKE :studentCodeCyber OR " +
            "st.studentCode LIKE :studentCodeElectric " +
            "ORDER BY sr.gpa DESC")
    List<SemesterRanking> findBlockRanking(@Param("studentCodePrefix") String studentCodePrefix,
                                   @Param("studentCodeCyber") String studentCodeCyber,
                                   @Param("studentCodeElectric") String studentCodeElectric);
    @Query("SELECT sr FROM SemesterRanking sr JOIN sr.student st " +
            "WHERE st.studentCode LIKE CONCAT(:filterCode, '%') " +
            "ORDER BY sr.gpa DESC")
    List<SemesterRanking> findTopWithMatchingFilterCode(@Param("filterCode") String filterCode, Pageable pageable);
}
