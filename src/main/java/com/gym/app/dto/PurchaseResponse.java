package com.gym.app.dto;

import com.gym.app.model.GymPlanPurchase;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseResponse {
    private Long                          id;
    private Long                          userId;
    private String                        username;
    private String                        planName;
    private BigDecimal                    totalPrice;
    private BigDecimal                    paymentPaid;
    private BigDecimal                    paymentRemaining;
    private GymPlanPurchase.PaymentStatus paymentStatus;
    private LocalDate                     startDate;
    private LocalDate                     endDate;
    private LocalDateTime                 purchasedAt;
}
