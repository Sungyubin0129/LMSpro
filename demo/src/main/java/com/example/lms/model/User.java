// src/main/java/com/example/lms/model/User.java
package com.example.lms.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users", schema = "lms_db")
@Getter @Setter @Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(length = 100)
    private String name;

    @Column(unique = true, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Entity가 최초 저장될 때 실행되어 createdAt 자동 세팅
    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
    }
}
