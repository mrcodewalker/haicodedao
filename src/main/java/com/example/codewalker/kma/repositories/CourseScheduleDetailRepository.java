package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.CourseScheduleDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CourseScheduleDetailRepository extends JpaRepository<CourseScheduleDetail, Long> {
    @Query(value = "SELECT * FROM course_schedule_details c WHERE c.semester LIKE %:semester% GROUP BY c.course_name", nativeQuery = true)
    List<CourseScheduleDetail> findGroupedByCourseName(@Param("semester") String semester);
    @Query(value = "SELECT DISTINCT c.semester FROM course_schedule_details c", nativeQuery = true)
    List<String> findDistinctSemesters();
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM course_schedule_details c WHERE c.semester LIKE %:semester%", nativeQuery = true)
    void deleteBySemester(@Param("semester") String semester);
    @Query(value = "SELECT * FROM course_schedule_details c WHERE c.course_id IN :courseCodes", nativeQuery = true)
    List<CourseScheduleDetail> findCourseCodeIn(@Param("courseCodes") List<String> courseCodes);
}

