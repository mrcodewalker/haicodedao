package com.example.codewalker.repositories;

import com.example.codewalker.models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    @Query("SELECT s FROM Schedule s WHERE s.subjectName LIKE %:keyword%")
    // AND s.startDay >= CURRENT_DATE
    List<Schedule> findBySubjectNameContainingKeyword(@Param("keyword") String keyword);
}
