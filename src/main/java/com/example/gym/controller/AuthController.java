package com.example.gym.controller;

import com.example.gym.model.Role;
import com.example.gym.model.User;
import com.example.gym.security.JwtUtil;
import com.example.gym.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String,Object> body) {
        String name = (String) body.get("name");
        String email = (String) body.get("email");
        String rawPassword = (String) body.get("password");
        Integer age = (Integer) body.getOrDefault("age", null);
        Double weight = body.get("weight") == null ? null : Double.valueOf(body.get("weight").toString());
        Double height = body.get("height") == null ? null : Double.valueOf(body.get("height").toString());
        String roleStr = (String) body.getOrDefault("role", "ROLE_USER");
        Role role = Role.valueOf(roleStr);

        if (userService.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error","Email already in use"));
        }

        User u = User.builder()
                .name(name)
                .email(email)
                .password(rawPassword)
                .age(age)
                .weight(weight)
                .height(height)
                .joinDate(LocalDate.now())
                .role(role)
                .build();

        User saved = userService.register(u);
        // don't return password
        saved.setPassword(null);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body) {
        String email = body.get("email");
        String password = body.get("password");

        var opt = userService.findByEmail(email);
        if (opt.isEmpty()) return ResponseEntity.status(401).body(Map.of("error","Invalid credentials"));

        User user = opt.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error","Invalid credentials"));
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return ResponseEntity.ok(Map.of("token", token));
    }
}

