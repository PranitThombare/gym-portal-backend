package com.example.gym.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.gym.model.Attendance;
import java.util.List;

public interface AttendanceRepository extends MongoRepository<Attendance, String> {
    List<Attendance> findByUserId(String userId);
}
