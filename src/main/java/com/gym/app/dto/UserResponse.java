package com.gym.app.dto;

import com.gym.app.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long          id;
    private String        username;
    private String        mobileNumber;
    private Integer       age;
    private String        address;
    private LocalDateTime createdAt;
}
