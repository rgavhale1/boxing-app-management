package com.gym.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "gym_plan_purchases")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GymPlanPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_plan_id", nullable = false)
    private GymPlan gymPlan;

    // Total price at time of purchase (snapshot)
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    // How much has been paid so far
    @Column(name = "payment_paid", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal paymentPaid = BigDecimal.ZERO;

    // Remaining = totalPrice - paymentPaid (auto-calculated)
    @Column(name = "payment_remaining", nullable = false, precision = 10, scale = 2)
    private BigDecimal paymentRemaining;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(name = "start_date", nullable = false)
    @Builder.Default
    private LocalDate startDate = LocalDate.now();

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "purchased_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime purchasedAt = LocalDateTime.now();

    @PrePersist
    @PreUpdate
    public void calculateRemaining() {
        if (totalPrice != null && paymentPaid != null) {
            this.paymentRemaining = totalPrice.subtract(paymentPaid);
            if (this.paymentRemaining.compareTo(BigDecimal.ZERO) == 0) {
                this.paymentStatus = PaymentStatus.FULLY_PAID;
            } else if (this.paymentPaid.compareTo(BigDecimal.ZERO) > 0) {
                this.paymentStatus = PaymentStatus.PARTIAL;
            } else {
                this.paymentStatus = PaymentStatus.PENDING;
            }
        }
    }

    public enum PaymentStatus {
        PENDING,
        PARTIAL,
        FULLY_PAID
    }
}
