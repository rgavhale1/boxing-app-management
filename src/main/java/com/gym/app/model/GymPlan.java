package com.gym.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "gym_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GymPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;            // e.g. "Monthly Basic", "Quarterly Premium"

    @Column(length = 500)
    private String description;

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "duration_months", nullable = false)
    private Integer durationMonths; // e.g. 1, 3, 6, 12

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}
