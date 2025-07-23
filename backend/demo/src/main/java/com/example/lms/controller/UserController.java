package com.example.lms.controller;

import com.example.lms.dto.PasswordUpdateRequest;
import com.example.lms.dto.UserProfileResponse;
import com.example.lms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5175")
public class UserController {
    private final UserService userService;
    //api/users/me : 내 정보 조회
    //api/users/me/password : 내 비밀번호 변경
    //api/enrollments : 내 수강 신청, 수강 내역
    //api/courses : 강의 목록/관리
    //api/auth/login, /api/auth/register : 인증/회원가입

    /** 내 프로필 정보 조회 */
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getProfile(Principal principal) {
        
        // UserProfileResponse profile = userService.getProfile(principal.getName());

        // 테스트용
        String testUsername = "test";
        UserProfileResponse profile = userService.getProfile(testUsername);

        return ResponseEntity.ok(profile);
    }

    /** 비밀번호 변경 */
    @PatchMapping("/me/password")
    public ResponseEntity<Void> updatePassword(
            Principal principal,
            @RequestBody PasswordUpdateRequest req) {
        userService.updatePassword(principal.getName(), req);
        return ResponseEntity.ok().build();
    }

    // ... 추가로 필요한 사용자 관련 기능
}

