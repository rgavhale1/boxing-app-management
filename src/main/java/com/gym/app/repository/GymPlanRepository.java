package com.gym.app.repository;

import com.gym.app.model.GymPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GymPlanRepository extends JpaRepository<GymPlan, Long> {
    List<GymPlan> findByActiveTrue();
}
