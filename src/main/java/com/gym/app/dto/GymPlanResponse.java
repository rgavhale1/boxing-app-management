package com.gym.app.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GymPlanResponse {
    private Long       id;
    private String     name;
    private String     description;
    private BigDecimal totalPrice;
    private Integer    durationMonths;
    private Boolean    active;
}
