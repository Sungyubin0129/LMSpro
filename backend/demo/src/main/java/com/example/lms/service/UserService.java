package com.example.lms.service;

import com.example.lms.dto.RegisterRequest;
import com.example.lms.model.Role;
import com.example.lms.model.User;
import com.example.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.lms.dto.UserProfileResponse;
import com.example.lms.dto.PasswordUpdateRequest;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    public UserProfileResponse getProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));
        return UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    public void updatePassword(String username, PasswordUpdateRequest req) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        // 실제 구현: 이전 비밀번호 확인 & 암호화 적용 필요
        if (!user.getPassword().equals(req.getOldPassword())) {
            throw new RuntimeException("기존 비밀번호 불일치");
        }
        user.setPassword(req.getNewPassword());
        userRepository.save(user);
    }

    /**
     * 회원가입 처리
     */
    @Transactional
    public User register(RegisterRequest req) {
        // 1) 중복 체크
        userRepository.findByUsername(req.getUsername())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("이미 사용 중인 사용자명입니다.");
                });

        // 2) 엔티티 생성
        User user = User.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .name(req.getName())
                .email(req.getEmail())
                .role(Role.valueOf(req.getRole()))
                .build();

        // 3) 저장 후 반환
        return userRepository.save(user);
    }

    //
    //    @Override
    //    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    //        User u = userRepository.findByUsername(username)
    //                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음: " + username));
    //
    //        // Spring Security가 요구하는 UserDetails 구현체로 변환
    //        return org.springframework.security.core.userdetails.User.builder()
    //                .username(u.getUsername())
    //                .password(u.getPassword())
    //                .roles(u.getRole().name())
    //                .build();
    //    }

    // 테스트용
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // [1] DB에 있는 사용자 먼저 찾기
        User u = userRepository.findByUsername(username)
                .orElse(null);

        // [2] DB에 사용자 없으면, "테스트 계정" 처리
        if (u == null) {
            // username이 "test"라면 임시 테스트 계정 허용
            if ("test".equals(username)) {
                // password는 bcrypt로 암호화된 "test123"
                String encodedPassword = passwordEncoder.encode("test123");
                return org.springframework.security.core.userdetails.User.builder()
                        .username("test")
                        .password(encodedPassword)
                        .roles("USER") // 필요에 따라 ROLE 조정
                        .build();
            }
            // 다른 username이면 기존처럼 예외
            throw new UsernameNotFoundException("사용자 없음: " + username);
        }

        // [3] DB에 사용자 있으면 원래대로 리턴
        return org.springframework.security.core.userdetails.User.builder()
                .username(u.getUsername())
                .password(u.getPassword())
                .roles(u.getRole().name())
                .build();
    }

    public UserDetails loadByUsername(String username) {
        return null;
    }
}
