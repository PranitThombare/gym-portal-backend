package com.example.gym.service;

import com.example.gym.model.User;
import com.example.gym.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return repo.findByEmail(email);
    }

    public List<User> findAll() { return repo.findAll(); }

    public Optional<User> findById(Long id) { return repo.findById(id); }

    // New: expose save
    public User save(User user) {
        return repo.save(user);
    }

    // New: expose existsByEmail
    public boolean existsByEmail(String email) {
        return repo.existsByEmail(email);
    }

    public Page<User> search(String q, Pageable pageable) {
        if (q == null || q.isBlank()) {
            return repo.findAll(pageable);
        }
        return repo.search(q.trim(), pageable);
    }
}
