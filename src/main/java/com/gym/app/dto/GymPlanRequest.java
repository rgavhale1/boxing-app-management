package com.gym.app.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GymPlanRequest {

    @NotBlank(message = "Plan name is required")
    private String name;

    private String description;

    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal totalPrice;

    @NotNull(message = "Duration in months is required")
    @Min(value = 1, message = "Duration must be at least 1 month")
    private Integer durationMonths;
}
