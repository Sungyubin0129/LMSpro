package com.example.lms.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CourseRequest {
    private String title;
    private String description;
    private String thumbnailUrl;
}
