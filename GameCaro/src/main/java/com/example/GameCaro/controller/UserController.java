package com.example.GameCaro.controller;


import com.example.GameCaro.domain.User;
import com.example.GameCaro.repository.UserRepository;
import com.example.GameCaro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository repo) {
        this.userRepository = repo;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("User is not authenticated");
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return ResponseEntity.status(404).body("User not found");
        return ResponseEntity.ok(user);
    }



    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(Authentication authentication, @RequestBody User updateData) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        user.setDisplayName(updateData.getDisplayName());

        userRepository.save(user);
        return ResponseEntity.ok("Profile updated");
    }
}


