package com.example.lms.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments", schema = "lms_db")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 수강생
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id")
    private User student;

    // 강의
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id")
    private Course course;

    // 진도율 0~100
    @Column(nullable = false)
    private int progress;

    // 완료 여부
    @Column(nullable = false)
    private boolean completed;

    // 완료 시각
    private LocalDateTime completedAt;

    @PrePersist
    public void prePersist() {
        this.progress   = 0;
        this.completed  = false;
    }
}
