package com.example.gym.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "attendance")
@Getter
@Setter
public class Attendance {
    @Id
    private String id;
    private String userId;
    private LocalDateTime timestamp;
    private String type; // "CHECKIN" or "CHECKOUT" (optional)

    // getters/setters

}

