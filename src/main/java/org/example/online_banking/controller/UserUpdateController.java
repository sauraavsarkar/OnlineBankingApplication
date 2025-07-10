package org.example.online_banking.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.online_banking.dto.UpdatePasswordRequest;
import org.example.online_banking.dto.UpdateUsernameRequest;
import org.example.online_banking.model.User;
import org.example.online_banking.repository.UserRepository;
import org.example.online_banking.security.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserUpdateController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserUpdateController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Helper method to get username from token
    private String extractUsernameFromToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (JwtUtil.validateToken(token)) {
                return JwtUtil.extractUsername(token);
            }
        }
        return null;
    }

    // Update username
    @PutMapping("/update-username")
    @Transactional
    public ResponseEntity<?> updateUsername(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                            @RequestBody UpdateUsernameRequest request) {
        String currentUsername = extractUsernameFromToken(authHeader);
        if (currentUsername == null) {
            return ResponseEntity.status(401).body("Invalid token");
        }

        // Check if new username already exists
        if (userRepository.findByUsername(request.getNewUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already taken");
        }

        Optional<User> userOpt = userRepository.findByUsername(currentUsername);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        User user = userOpt.get();
        user.setUsername(request.getNewUsername());
        userRepository.save(user);

        return ResponseEntity.ok("Username updated successfully");
    }

    // Update password
    @PutMapping("/update-password")
    @Transactional
    public ResponseEntity<?> updatePassword(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                            @RequestBody UpdatePasswordRequest request) {
        String currentUsername = extractUsernameFromToken(authHeader);
        if (currentUsername == null) {
            return ResponseEntity.status(401).body("Invalid token");
        }

        Optional<User> userOpt = userRepository.findByUsername(currentUsername);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        User user = userOpt.get();
        user.setPasswordHash(request.getNewPassword());
        userRepository.save(user);
        return ResponseEntity.ok("Password updated successfully");
    }
}
