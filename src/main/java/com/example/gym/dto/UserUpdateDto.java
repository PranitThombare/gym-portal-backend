
package com.example.gym.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto {
    @Size(max = 255)
    private String name;

    @Positive
    private Integer age;

    // positive or zero allowed
    private Double weight;
    private Double height;

    @Email
    private String email;


}

