package com.example.lms.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class UserProfileResponse {
    private Long id;
    private String username;
    private String name;
    private String email;
    private String role;
}
