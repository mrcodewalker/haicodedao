package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.Graph;
import com.example.codewalker.kma.models.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface GraphRepository extends JpaRepository<Graph, Long> {
    @Query("SELECT g FROM Graph g WHERE g.subject.id = :subjectId AND g.semester LIKE CONCAT('%', :year, '%')")
    List<Graph> findBySubjectIdAndYear(@Param("subjectId") Long subjectId, @Param("year") String year);
    @Query("SELECT DISTINCT g.subject.id FROM Graph g")
    List<Long> findDistinctSubjectIds();
}
