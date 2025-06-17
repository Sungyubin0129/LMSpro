package com.example.lms.repository;

import com.example.lms.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    // 필요 시, 강사별 강의 조회용 메소드 추가 가능
    // List<Course> findAllByInstructorId(Long instructorId);
}
