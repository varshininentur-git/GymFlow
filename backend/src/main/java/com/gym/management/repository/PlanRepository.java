package com.gym.management.repository;
import com.gym.management.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PlanRepository extends JpaRepository<Plan, Long> { boolean existsByNameIgnoreCase(String name); }
