// src/main/java/com/example/lms/service/CourseService.java
package com.example.lms.service;

import com.example.lms.dto.*;
import com.example.lms.model.Course;
import com.example.lms.model.User;
import com.example.lms.repository.CourseRepository;
import com.example.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepo;
    private final UserRepository userRepo;

    /** 새 강의 생성 */
    @Transactional
    public CourseResponse createCourse(Long instructorId, CourseRequest req) {
        User instructor = userRepo.findById(instructorId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강사입니다. id=" + instructorId));

        Course course = Course.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .thumbnailUrl(req.getThumbnailUrl())
                .instructor(instructor)
                .build();

        Course saved = courseRepo.save(course);
        return toDto(saved);
    }

    /** 모든 강의 조회 */
    @Transactional(readOnly = true)
    public List<CourseResponse> listAll() {
        return courseRepo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /** 단일 강의 조회 */
    @Transactional(readOnly = true)
    public CourseResponse getById(Long courseId) {
        Course c = courseRepo.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("강의를 찾을 수 없습니다. id=" + courseId));
        return toDto(c);
    }

    /** 강의 수정 */
    @Transactional
    public CourseResponse updateCourse(Long courseId, CourseRequest req) {
        Course c = courseRepo.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("강의를 찾을 수 없습니다. id=" + courseId));

        c.setTitle(req.getTitle());
        c.setDescription(req.getDescription());
        c.setThumbnailUrl(req.getThumbnailUrl());
        // instructor 변경은 별도 API로 다루는 게 안전합니다.

        return toDto(c);
    }

    /** 강의 삭제 */
    @Transactional
    public void deleteCourse(Long courseId) {
        if (!courseRepo.existsById(courseId)) {
            throw new IllegalArgumentException("강의를 찾을 수 없습니다. id=" + courseId);
        }
        courseRepo.deleteById(courseId);
    }

    /** Entity → DTO 변환 헬퍼 */
    private CourseResponse toDto(Course c) {
        return CourseResponse.builder()
                .id(c.getId())
                .title(c.getTitle())
                .description(c.getDescription())
                .thumbnailUrl(c.getThumbnailUrl())
                .instructorId(c.getInstructor().getId())
                .instructorName(c.getInstructor().getName())
                .createdAt(c.getCreatedAt())
                .build();
    }
}
