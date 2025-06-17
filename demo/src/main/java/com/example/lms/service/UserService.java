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

        // 비밀번호는 BCrypt 해시로 저장되므로 matches 사용
        if (!passwordEncoder.matches(req.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("기존 비밀번호 불일치");
        }
        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음: " + username));

        // Spring Security가 요구하는 UserDetails 구현체로 변환
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
