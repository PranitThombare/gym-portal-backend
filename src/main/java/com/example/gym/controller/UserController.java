package com.example.gym.controller;

import com.example.gym.model.Attendance;
import com.example.gym.model.User;
import com.example.gym.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User saved = userService.register(user);
        // don't return password in real app — this is basic example
        saved.setPassword(null);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        Optional<User> optUser = userService.login(username, password);

        if (optUser.isPresent()) {
            User user = optUser.get();
            user.setPassword(null); // never return password in responses
            return ResponseEntity.ok(user);                     // <-- returns a ResponseEntity<User>
        } else {
            // build a ResponseEntity with 401 and a small error body
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)            // <-- BodyBuilder
                    .body(Map.of("error", "Invalid credentials")); // <-- .body(...) produces ResponseEntity
        }
    }

    @PostMapping("/{userId}/attendance")
    public ResponseEntity<Attendance> markAttendance(@PathVariable String userId) {
        Attendance a = userService.markAttendance(userId);
        return ResponseEntity.ok(a);
    }

    @GetMapping("/{userId}/attendance")
    public ResponseEntity<?> getAttendance(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserAttendance(userId));
    }
}
