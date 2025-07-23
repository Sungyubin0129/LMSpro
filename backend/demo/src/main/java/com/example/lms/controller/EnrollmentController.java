package com.example.lms.controller;

import com.example.lms.dto.*;
import com.example.lms.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5175")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    /** 수강 신청 */
    @PostMapping("/api/courses/{courseId}/enroll")
    public ResponseEntity<EnrollmentResponse> enroll(
            @PathVariable Long courseId,
            Principal principal) {
        EnrollmentResponse resp = enrollmentService.enroll(courseId, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    /** 내 수강 목록 */
    @GetMapping("/api/enrollments")
    public ResponseEntity<List<EnrollmentResponse>> listMy(
            Principal principal) {
        List<EnrollmentResponse> list = enrollmentService.listMyEnrollments(principal.getName());
        return ResponseEntity.ok(list);
    }

    /** 진도율 갱신 */
    @PatchMapping("/api/enrollments/{id}/progress")
    public ResponseEntity<EnrollmentResponse> updateProgress(
            @PathVariable Long id,
            @RequestBody ProgressRequest req) {
        EnrollmentResponse resp = enrollmentService.updateProgress(id, req.getProgress());
        return ResponseEntity.ok(resp);
    }

    /** 수강 완료 */
    @PatchMapping("/api/enrollments/{id}/complete")
    public ResponseEntity<EnrollmentResponse> complete(
            @PathVariable Long id) {
        EnrollmentResponse resp = enrollmentService.complete(id);
        return ResponseEntity.ok(resp);
    }
}
