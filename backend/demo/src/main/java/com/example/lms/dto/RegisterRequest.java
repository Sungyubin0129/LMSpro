package com.example.lms.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RegisterRequest {
    private String username;
    private String password;
    private String name;
    private String email;
    private String role; // "ADMIN", "INSTRUCTOR" 또는 "STUDENT"
}
