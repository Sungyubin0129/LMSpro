package com.example.lms.repository;

import com.example.lms.model.Enrollment;
import com.example.lms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    // 특정 수강생의 전체 수강 내역
    List<Enrollment> findAllByStudent(User student);

    // 학생+강의 중복 수강 방지용 조회
    Optional<Enrollment> findByStudentAndCourseId(User student, Long courseId);
}