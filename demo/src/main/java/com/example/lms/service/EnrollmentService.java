package com.example.lms.service;

import com.example.lms.dto.*;
import com.example.lms.model.*;
import com.example.lms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepository enrollRepo;
    private final CourseRepository courseRepo;
    private final UserRepository userRepo;

    /** 수강 신청 */
    @Transactional
    public EnrollmentResponse enroll(Long courseId, String username) {
        // !! 현재 로그인한 학생 정보 가져오기
        User student = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음: " + username));

        // 중복 수강 방지
        if (enrollRepo.findByStudentAndCourseId(student, courseId).isPresent()) {
            throw new IllegalArgumentException("이미 수강 중인 강의입니다. courseId=" + courseId);
        }

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("강의를 찾을 수 없습니다. id=" + courseId));

        Enrollment e = Enrollment.builder()
                .student(student)
                .course(course)
                .build();

        // 저장 후 DTO 변환
        Enrollment saved = enrollRepo.save(e);
        return toDto(saved);
    }

    /** 내 수강 목록 조회 */
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> listMyEnrollments(String username) {
        User student = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음: " + username));

        return enrollRepo.findAllByStudent(student).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /** 진도율 갱신 */
    @Transactional
    public EnrollmentResponse updateProgress(Long enrollmentId, int progress) {
        Enrollment e = enrollRepo.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("수강 내역이 없습니다. id=" + enrollmentId));

        e.setProgress(progress);
        return toDto(e);
    }

    /** 수강 완료 처리 */
    @Transactional
    public EnrollmentResponse complete(Long enrollmentId) {
        Enrollment e = enrollRepo.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("수강 내역이 없습니다. id=" + enrollmentId));

        e.setCompleted(true);
        e.setCompletedAt(LocalDateTime.now());
        return toDto(e);
    }

    private EnrollmentResponse toDto(Enrollment e) {
        return EnrollmentResponse.builder()
                .id(e.getId())
                .courseId(e.getCourse().getId())
                .courseTitle(e.getCourse().getTitle())
                .progress(e.getProgress())
                .completed(e.isCompleted())
                .completedAt(e.getCompletedAt())
                .build();
    }
}
