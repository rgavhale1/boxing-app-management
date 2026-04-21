package com.gym.app.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequest {

    @NotNull(message = "Gym plan ID is required")
    private Long gymPlanId;

    @NotNull(message = "Payment paid is required")
    @DecimalMin(value = "0.0", message = "Payment paid cannot be negative")
    private BigDecimal paymentPaid;

    private LocalDate startDate; // optional; defaults to today
}
