package com.example.gym.service;

import com.example.gym.model.Attendance;
import com.example.gym.model.User;
import com.example.gym.repository.AttendanceRepository;
import com.example.gym.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final AttendanceRepository attendanceRepo;

    public UserService(UserRepository userRepo, AttendanceRepository attendanceRepo) {
        this.userRepo = userRepo;
        this.attendanceRepo = attendanceRepo;
    }

    public User register(User user) {
        // validate, check existing, hash password later
        return userRepo.save(user);
    }

    public Optional<User> login(String username, String password) {
        return userRepo.findByUsername(username)
                .filter(u -> u.getPassword().equals(password)); // replace with bcrypt later
    }

    public Attendance markAttendance(String userId) {
        Attendance att = new Attendance();
        att.setUserId(userId);
        att.setTimestamp(LocalDateTime.now());
        att.setType("CHECKIN");
        Attendance saved = attendanceRepo.save(att);
        // attach to user
        userRepo.findById(userId).ifPresent(u -> {
            u.getAttendanceIds().add(saved.getId());
            userRepo.save(u);
        });
        return saved;
    }

    public List<Attendance> getUserAttendance(String userId) {
        return attendanceRepo.findByUserId(userId);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
}

