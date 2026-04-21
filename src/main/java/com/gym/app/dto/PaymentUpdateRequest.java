package com.gym.app.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentUpdateRequest {

    @NotNull(message = "Additional payment amount is required")
    @DecimalMin(value = "0.01", message = "Payment must be greater than 0")
    private BigDecimal additionalPayment;
}
