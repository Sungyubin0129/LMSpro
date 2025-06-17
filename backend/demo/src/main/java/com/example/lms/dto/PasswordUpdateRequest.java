package com.example.lms.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class PasswordUpdateRequest {
    private String oldPassword;
    private String newPassword;
}
