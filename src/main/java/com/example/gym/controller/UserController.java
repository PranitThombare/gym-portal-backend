package com.example.gym.controller;

import com.example.gym.dto.UserUpdateDto;
import com.example.gym.model.User;
import com.example.gym.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    // ADMIN: list all users
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(Authentication auth) {
        // Security annotations already restrict; return list
        List<User> users = service.findAll();
        users.forEach(u -> u.setPassword(null));
        return ResponseEntity.ok(users);
    }

    // Any logged-in user: get own profile
    @GetMapping("/me")
    public ResponseEntity<?> getProfile(Authentication auth) {
        String email = (String) auth.getPrincipal();
        return service.findByEmail(email)
                .map(u -> {
                    u.setPassword(null);
                    return ResponseEntity.ok(u);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Admin can get a user by id (optional)
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(u -> {
                    u.setPassword(null);
                    return ResponseEntity.ok(u);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(@RequestBody @Valid UserUpdateDto dto, Authentication auth) {
        String email = (String) auth.getPrincipal();

        var opt = service.findByEmail(email);
        if (opt.isEmpty()) return ResponseEntity.status(404).body(Map.of("error", "User not found"));

        User user = opt.get();

        // If email change requested and different, check uniqueness
        if (dto.getEmail() != null && !dto.getEmail().equalsIgnoreCase(user.getEmail())) {
            if (service.existsByEmail(dto.getEmail())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email already in use"));
            }
            user.setEmail(dto.getEmail());
        }

        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getAge() != null) user.setAge(dto.getAge());
        if (dto.getWeight() != null) user.setWeight(dto.getWeight());
        if (dto.getHeight() != null) user.setHeight(dto.getHeight());

        User saved = service.save(user);
        saved.setPassword(null); // don't leak password
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<User>> listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort,
            @RequestParam(required = false) String q
    ) {
        String[] parts = sort.split(",");
        String sortProp = parts[0];
        Sort.Direction dir = parts.length > 1 ? Sort.Direction.fromString(parts[1]) : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortProp));

        Page<User> users = service.search(q, pageable);
        // never return passwords
        users.forEach(u -> u.setPassword(null));
        return ResponseEntity.ok(users);
    }
}

