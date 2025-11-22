package com.example.gym.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
public class User {
    @Id
    private String id;
    private String username;
    private String email;
    private String password; // plain for now; hash later
    private String role = "USER"; // "USER" or "ADMIN"
    private List<String> attendanceIds = new ArrayList<>();

}
