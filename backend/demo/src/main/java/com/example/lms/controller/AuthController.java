package com.example.lms.controller;

import com.example.lms.dto.*;
import com.example.lms.model.User;
import com.example.lms.security.JwtUtil;
import com.example.lms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5175")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    /** 회원가입 엔드포인트 **/
    // 여기서 role 검증을 더 강하게 ..?
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        User saved = userService.register(req);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("회원가입 성공: " + saved.getUsername());
    }

    /** 로그인 엔드포인트 **/
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        // AuthenticationManager로 아이디/비번 검증
//        Authentication authToken =
//                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword());
//        Authentication auth = authenticationManager.authenticate(authToken);
//
//        // JWT 생성
//        String jwt = jwtUtil.generateToken(auth.getName());
//
//        // 리턴
//        AuthResponse resp = AuthResponse.builder()
//                .token(jwt)
//                .build();

        AuthResponse resp = AuthResponse.builder()
                .token("dummy-token")
                .build();

        return ResponseEntity.ok(resp);
    }
}
