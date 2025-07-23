package com.example.lms.controller;

import com.example.lms.dto.*;
import com.example.lms.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5175")
public class CourseController {
    private final CourseService courseService;

    /** 전체 강의 목록 */
    @GetMapping
    public ResponseEntity<List<CourseResponse>> listAll() {
        return ResponseEntity.ok(courseService.listAll());
    }

    /** 단일 강의 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getById(id));
    }

    /** 강의 등록 (강사 권한 필요) */
    @PostMapping
    public ResponseEntity<CourseResponse> create(
            @RequestParam Long instructorId,
            @RequestBody CourseRequest req) {
        CourseResponse created = courseService.createCourse(instructorId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /** 강의 수정 (강사 본인만 가능하도록 추가 인증 로직 붙일 수 있음) */
    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> update(
            @PathVariable Long id,
            @RequestBody CourseRequest req) {
        return ResponseEntity.ok(courseService.updateCourse(id, req));
    }

    /** 강의 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
