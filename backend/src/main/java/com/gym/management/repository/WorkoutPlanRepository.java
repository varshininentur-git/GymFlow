package com.gym.management.repository;
import com.gym.management.entity.WorkoutPlan;import org.springframework.data.jpa.repository.JpaRepository;import org.springframework.data.jpa.repository.EntityGraph;import java.util.*;
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan,Long>{@EntityGraph(attributePaths={"exercises","exercises.exercise"})List<WorkoutPlan> findByMemberIdAndActiveTrueOrderByNameAsc(Long memberId);@EntityGraph(attributePaths={"exercises","exercises.exercise"})Optional<WorkoutPlan> findByIdAndMemberId(Long id,Long memberId);}
