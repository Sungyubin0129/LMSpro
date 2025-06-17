package com.example.lms.dto;

import lombok.*;

@Getter @Setter
@Builder
public class AuthResponse {
    private String token;

    @Builder.Default
    private String tokenType = "Bearer";
}
