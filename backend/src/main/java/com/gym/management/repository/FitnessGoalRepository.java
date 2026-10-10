package com.gym.management.repository;
import com.gym.management.entity.FitnessGoal;import org.springframework.data.jpa.repository.JpaRepository;import java.util.*;
public interface FitnessGoalRepository extends JpaRepository<FitnessGoal,Long>{List<FitnessGoal> findByMemberIdOrderByStatusAscTargetDateAsc(Long memberId);Optional<FitnessGoal> findByIdAndMemberId(Long id,Long memberId);long countByMemberIdAndStatus(Long memberId,String status);}
