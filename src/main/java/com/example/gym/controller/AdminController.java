package com.example.gym.controller;

import com.example.gym.model.Attendance;
import com.example.gym.model.User;
import com.example.gym.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;
    public AdminController(UserService userService) { this.userService = userService; }

    @GetMapping("/users")
    public ResponseEntity<List<User>> listUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{userId}/attendance")
    public ResponseEntity<List<Attendance>> attendanceForUser(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserAttendance(userId));
    }
}
