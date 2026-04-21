package com.gym.app.repository;

import com.gym.app.model.GymPlanPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GymPlanPurchaseRepository extends JpaRepository<GymPlanPurchase, Long> {

    List<GymPlanPurchase> findByUserId(Long userId);

    List<GymPlanPurchase> findByUserIdAndPaymentStatus(
            Long userId,
            GymPlanPurchase.PaymentStatus paymentStatus
    );
}
