package com.example.GameCaro.service;

import com.example.GameCaro.domain.User;
import com.example.GameCaro.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public User findById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    public void deleteById(Long id) {
        this.userRepository.deleteById(id);
    }

    public boolean existsByDisplayName(String displayName) {
        return this.userRepository.existsByDisplayName(displayName);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return this.userRepository.existsByUsername(username);
    }

    public User updateUser(User user) {
        return this.userRepository.save(user);
    }
}
